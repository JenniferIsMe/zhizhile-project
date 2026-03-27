<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import PatternViewport from '../components/workbench/PatternViewport.vue'
import FocusMaskOverlay from '../components/workbench/FocusMaskOverlay.vue'
import MaskHeightControl from '../components/workbench/MaskHeightControl.vue'
import CounterCard from '../components/workbench/CounterCard.vue'
import TimerCard from '../components/workbench/TimerCard.vue'
import ProgressSaveIndicator from '../components/workbench/ProgressSaveIndicator.vue'
import PatternMetaCard from '../components/workbench/PatternMetaCard.vue'
import EmptyState from '../components/common/EmptyState.vue'
import ErrorState from '../components/common/ErrorState.vue'
import { useRouterState } from '../router'
import { getProjectOverview } from '../api/project'
import { getFileContentUrl } from '../api/file'
import { getPatternConfig, savePatternConfig } from '../api/patternConfig'
import { saveRowProgress, saveTimeProgress } from '../api/progress'
import { projectState } from '../stores/project'
import { readerState } from '../stores/reader'
import { settingsState } from '../stores/settings'
import { pushToast } from '../stores/toast'
import { createDebounce } from '../utils/debounceSync'
import { mountReaderHotkeys } from '../components/workbench/ReaderHotkeyController'
import type { Pattern } from '../types/api'

const router = useRouterState()
const loading = ref(false)
const error = ref('')
const sourceUrl = ref('')
const sourceType = ref<'pdf' | 'image' | 'external' | 'unknown'>('unknown')
const currentRowIndex = ref(1)
const totalCount = ref(0)
const totalSeconds = ref(0)
const timerRunning = ref(false)

let timerTickId = 0
let timeSyncIntervalId = 0
let releaseHotkeys: (() => void) | null = null

/**
 * 从路由读取 projectId。
 */
const projectId = computed(() => {
  const raw = router.params.value.projectId
  const parsed = Number(raw)
  return Number.isFinite(parsed) ? parsed : 0
})

/**
 * 当前图解。
 */
const currentPattern = computed<Pattern | null>(() => projectState.currentPattern)

/**
 * 将当前图解转换为阅读器可消费资源。
 */
function resolvePatternSource(pattern: Pattern | null): void {
  if (!pattern) {
    sourceType.value = 'unknown'
    sourceUrl.value = ''
    return
  }

  if (pattern.sourceType === 'UPLOAD' && pattern.fileId) {
    sourceUrl.value = getFileContentUrl(pattern.fileId)
    sourceType.value = pattern.displayName.toLowerCase().endsWith('.pdf') ? 'pdf' : 'image'
    return
  }

  if (pattern.sourceType === 'LINK' && pattern.externalUrl) {
    sourceUrl.value = pattern.externalUrl
    sourceType.value = 'external'
    return
  }

  sourceType.value = 'unknown'
  sourceUrl.value = ''
}

/**
 * 恢复工作台总览数据。
 */
async function bootstrap(): Promise<void> {
  if (!projectId.value) {
    error.value = '无效项目 ID'
    return
  }

  loading.value = true
  error.value = ''

  try {
    const overview = await getProjectOverview(projectId.value)

    projectState.currentProject = overview.project
    projectState.patterns = overview.patterns
    projectState.currentPattern = overview.currentPattern

    if (overview.patternConfig) {
      readerState.currentPage = overview.patternConfig.currentPage ?? 1
      readerState.maskTopOffset = overview.patternConfig.maskTopOffset ?? 120
      readerState.maskHeight = overview.patternConfig.maskHeight ?? 48
    } else if (overview.currentPattern) {
      try {
        const config = await getPatternConfig(projectId.value, overview.currentPattern.id)
        readerState.currentPage = config.currentPage ?? 1
        readerState.maskTopOffset = config.maskTopOffset ?? 120
        readerState.maskHeight = config.maskHeight ?? 48
      } catch {
        // 配置缺失时使用默认值。
      }
    }

    if (overview.progress) {
      currentRowIndex.value = overview.progress.currentRowIndex ?? 1
      totalCount.value = overview.progress.totalCount ?? 0
      totalSeconds.value = Number(overview.progress.totalSeconds ?? 0)
    }

    resolvePatternSource(overview.currentPattern)
    readerState.saveStatus = 'saved'
  } catch (cause) {
    error.value = (cause as Error).message
  } finally {
    loading.value = false
  }
}

/**
 * 保存阅读配置。
 */
async function submitPatternConfig(): Promise<void> {
  if (!projectId.value || !currentPattern.value) {
    return
  }

  readerState.saveStatus = 'saving'

  try {
    await savePatternConfig(projectId.value, currentPattern.value.id, {
      currentPage: readerState.currentPage,
      maskTopOffset: readerState.maskTopOffset,
      maskHeight: readerState.maskHeight,
    })

    readerState.saveStatus = 'saved'
  } catch {
    readerState.saveStatus = 'error'
  }
}

/**
 * 保存计数。
 */
async function submitRowProgress(): Promise<void> {
  if (!projectId.value) {
    return
  }

  try {
    await saveRowProgress(projectId.value, currentRowIndex.value, totalCount.value)
  } catch (cause) {
    pushToast('error', `计数同步失败：${(cause as Error).message}`)
  }
}

/**
 * 保存计时。
 */
async function submitTimeProgress(): Promise<void> {
  if (!projectId.value) {
    return
  }

  try {
    await saveTimeProgress(projectId.value, totalSeconds.value)
  } catch (cause) {
    pushToast('error', `计时同步失败：${(cause as Error).message}`)
  }
}

/**
 * 按 PRD 要求对高频行为做防抖。
 */
const debouncedConfigSync = createDebounce(() => {
  void submitPatternConfig()
}, 1000)

const debouncedRowSync = createDebounce(() => {
  void submitRowProgress()
}, 1500)

/**
 * 调整遮罩位置时做边界保护。
 */
function clampMaskOffset(next: number): number {
  return Math.max(0, Math.min(next, 1200))
}

/**
 * 增加计数。
 * 在动作唤醒模式下，首次计数会自动启动计时器。
 */
function increaseCount(): void {
  totalCount.value += 1

  if (settingsState.autoWake && !timerRunning.value) {
    timerRunning.value = true
  }

  if (settingsState.enableSound) {
    playTapSound()
  }

  debouncedRowSync()
}

/**
 * 使用 WebAudio 生成短促提示音。
 */
function playTapSound(): void {
  const context = new AudioContext()
  const oscillator = context.createOscillator()
  const gain = context.createGain()

  oscillator.type = 'square'
  oscillator.frequency.value = 780
  gain.gain.value = 0.03

  oscillator.connect(gain)
  gain.connect(context.destination)

  oscillator.start()
  oscillator.stop(context.currentTime + 0.04)
}

/**
 * 开关计时器。
 */
function toggleTimer(): void {
  timerRunning.value = !timerRunning.value
}

/**
 * 组件销毁或路由切走时执行一次最终同步。
 */
function flushAll(): void {
  void submitPatternConfig()
  void submitRowProgress()
  void submitTimeProgress()
}

/**
 * 阅读状态变化时同步配置。
 */
watch(
  () => [readerState.currentPage, readerState.maskTopOffset, readerState.maskHeight],
  () => {
    debouncedConfigSync()
  },
)

/**
 * 行号变化时同步计数数据。
 */
watch(currentRowIndex, () => {
  debouncedRowSync()
})

/**
 * 当路由上的项目 ID 变化时，重新加载工作台数据。
 * 这样可以支持在同一个 Workbench 组件实例内切换不同项目。
 */
watch(
  projectId,
  () => {
    void bootstrap()
  },
  { immediate: true },
)

/**
 * 计时器运行状态变化时，分别启动/停止本地秒表。
 */
watch(timerRunning, (running) => {
  window.clearInterval(timerTickId)

  if (running) {
    timerTickId = window.setInterval(() => {
      totalSeconds.value += 1
    }, 1000)
  }
})

onMounted(() => {
  releaseHotkeys = mountReaderHotkeys({
    onMoveUp: () => {
      readerState.maskTopOffset = clampMaskOffset(readerState.maskTopOffset - settingsState.maskStep)
    },
    onMoveDown: () => {
      readerState.maskTopOffset = clampMaskOffset(readerState.maskTopOffset + settingsState.maskStep)
    },
    onCount: () => {
      increaseCount()
    },
  })

  timeSyncIntervalId = window.setInterval(() => {
    if (timerRunning.value) {
      void submitTimeProgress()
    }
  }, 3000)
})

onBeforeUnmount(() => {
  window.clearInterval(timerTickId)
  window.clearInterval(timeSyncIntervalId)

  if (releaseHotkeys) {
    releaseHotkeys()
  }

  flushAll()
})
</script>

<template>
  <section class="workbench-view">
    <p v-if="loading">正在加载工作台...</p>

    <ErrorState
      v-else-if="error"
      title="工作台加载失败"
      :description="error"
      action-text="重试"
      @retry="bootstrap"
    />

    <EmptyState v-else-if="!currentPattern" title="当前项目没有图解" description="请先回到图解库添加并选择当前图解。" />

    <template v-else>
      <div class="left-panel">
        <div class="reader-container">
          <PatternViewport
            :source-url="sourceUrl"
            :source-type="sourceType"
            :current-page="readerState.currentPage"
            :zoom="readerState.zoom"
            @update-page="readerState.currentPage = $event"
            @zoom-in="readerState.zoom = Math.min(2.4, Number((readerState.zoom + 0.1).toFixed(2)))"
            @zoom-out="readerState.zoom = Math.max(0.5, Number((readerState.zoom - 0.1).toFixed(2)))"
          />
          <FocusMaskOverlay
            :mask-top-offset="readerState.maskTopOffset"
            :mask-height="readerState.maskHeight"
            :opacity="settingsState.maskOpacity"
          />
        </div>
      </div>

      <aside class="right-panel">
        <ProgressSaveIndicator :status="readerState.saveStatus" />

        <MaskHeightControl
          :value="readerState.maskHeight"
          :min="24"
          :max="260"
          @change="readerState.maskHeight = $event"
        />

        <CounterCard
          :current-row-index="currentRowIndex"
          :total-count="totalCount"
          @increment-count="increaseCount"
          @change-row="currentRowIndex = $event"
        />

        <TimerCard :total-seconds="totalSeconds" :running="timerRunning" :auto-wake="settingsState.autoWake" @toggle="toggleTimer" />

        <PatternMetaCard :project="projectState.currentProject" :pattern="projectState.currentPattern" />
      </aside>
    </template>
  </section>
</template>

<style scoped>
.workbench-view {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 12px;
  min-height: calc(100vh - 110px);
}

.left-panel {
  min-width: 0;
}

.reader-container {
  position: relative;
  height: 100%;
  min-height: 620px;
}

.right-panel {
  display: grid;
  align-content: start;
  gap: 10px;
}

@media (max-width: 1100px) {
  .workbench-view {
    grid-template-columns: 1fr;
  }
}
</style>
