<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import CreateProjectModal from '../components/library/CreateProjectModal.vue'
import ProjectCardGrid from '../components/library/ProjectCardGrid.vue'
import PatternManagerDrawer from '../components/library/PatternManagerDrawer.vue'
import ConfirmDialog from '../components/common/ConfirmDialog.vue'
import EmptyState from '../components/common/EmptyState.vue'
import ErrorState from '../components/common/ErrorState.vue'
import { createProject, getProjectDetail } from '../api/project'
import {
  createPatternLink,
  deletePattern,
  listPatterns,
  switchCurrentPattern,
  uploadPattern,
} from '../api/pattern'
import { getRecentProjectIds, projectState, saveRecentProjectId } from '../stores/project'
import { pushToast } from '../stores/toast'
import type { Pattern, ProjectDetail } from '../types/api'

/**
 * 页面局部状态。
 */
const loadingCreate = ref(false)
const loadingPatternOps = ref(false)
const loadingProjects = ref(false)
const loadError = ref('')
const showCreateModal = ref(false)
const showPatternDrawer = ref(false)
const activeProjectId = ref<number | null>(null)
const patternList = ref<Pattern[]>([])
const deletingPattern = ref<Pattern | null>(null)

/**
 * 当前抽屉选中的项目。
 */
const selectedProject = computed<ProjectDetail | null>(() => {
  if (!activeProjectId.value) {
    return null
  }

  return projectState.projects.find((project) => project.id === activeProjectId.value) ?? null
})

/**
 * 启动时根据本地记忆恢复近期项目列表。
 * 后端当前没有“项目列表接口”，所以通过近期 ID 逐个拉详情补齐。
 */
async function bootstrapProjects(): Promise<void> {
  loadingProjects.value = true
  loadError.value = ''

  try {
    const ids = getRecentProjectIds()
    const results: ProjectDetail[] = []

    for (const id of ids) {
      try {
        const detail = await getProjectDetail(id)
        results.push(detail)
      } catch {
        // 单个项目拉取失败不影响其它项目，容错跳过。
      }
    }

    projectState.projects = results
  } catch (error) {
    loadError.value = (error as Error).message
  } finally {
    loadingProjects.value = false
  }
}

/**
 * 创建项目。
 */
async function handleCreateProject(name: string): Promise<void> {
  loadingCreate.value = true

  try {
    const created = await createProject(name)
    projectState.projects = [created, ...projectState.projects.filter((item) => item.id !== created.id)]
    saveRecentProjectId(created.id)
    showCreateModal.value = false
    pushToast('success', '项目创建成功')
  } catch (error) {
    pushToast('error', `创建项目失败：${(error as Error).message}`)
  } finally {
    loadingCreate.value = false
  }
}

/**
 * 打开图解管理抽屉，并加载该项目图解列表。
 */
async function openPatternManager(project: ProjectDetail): Promise<void> {
  activeProjectId.value = project.id
  projectState.currentProject = project
  saveRecentProjectId(project.id)
  showPatternDrawer.value = true
  await refreshPatterns(project.id)
}

/**
 * 刷新图解列表，并同步当前图解。
 */
async function refreshPatterns(projectId: number): Promise<void> {
  loadingPatternOps.value = true

  try {
    const patterns = await listPatterns(projectId)
    patternList.value = patterns

    const latestProject = await getProjectDetail(projectId)
    projectState.projects = projectState.projects.map((project) => (project.id === latestProject.id ? latestProject : project))
    projectState.currentPattern =
      patterns.find((pattern) => pattern.id === latestProject.currentPatternId) ?? patterns[0] ?? null
  } catch (error) {
    pushToast('error', `加载图解失败：${(error as Error).message}`)
  } finally {
    loadingPatternOps.value = false
  }
}

/**
 * 上传图解文件。
 */
async function handleUploadPattern(file: File): Promise<void> {
  if (!activeProjectId.value) {
    return
  }

  loadingPatternOps.value = true

  try {
    await uploadPattern(activeProjectId.value, file)
    await refreshPatterns(activeProjectId.value)
    pushToast('success', '上传成功')
  } catch (error) {
    pushToast('error', `上传失败：${(error as Error).message}`)
  } finally {
    loadingPatternOps.value = false
  }
}

/**
 * 添加链接图解。
 */
async function handleAddLink(url: string, displayName: string): Promise<void> {
  if (!activeProjectId.value) {
    return
  }

  loadingPatternOps.value = true

  try {
    await createPatternLink(activeProjectId.value, url, displayName)
    await refreshPatterns(activeProjectId.value)
    pushToast('success', '链接图解添加成功')
  } catch (error) {
    pushToast('error', `添加失败：${(error as Error).message}`)
  } finally {
    loadingPatternOps.value = false
  }
}

/**
 * 切换当前图解。
 */
async function handleSwitchCurrent(pattern: Pattern): Promise<void> {
  if (!activeProjectId.value) {
    return
  }

  loadingPatternOps.value = true

  try {
    await switchCurrentPattern(activeProjectId.value, pattern.id)
    await refreshPatterns(activeProjectId.value)
    pushToast('success', '已切换当前图解')
  } catch (error) {
    pushToast('error', `切换失败：${(error as Error).message}`)
  } finally {
    loadingPatternOps.value = false
  }
}

/**
 * 请求删除图解（先打开确认弹窗）。
 */
function askDeletePattern(pattern: Pattern): void {
  deletingPattern.value = pattern
}

/**
 * 确认删除图解。
 */
async function confirmDeletePattern(): Promise<void> {
  if (!activeProjectId.value || !deletingPattern.value) {
    return
  }

  loadingPatternOps.value = true

  try {
    await deletePattern(activeProjectId.value, deletingPattern.value.id)
    deletingPattern.value = null
    await refreshPatterns(activeProjectId.value)
    pushToast('success', '图解已删除')
  } catch (error) {
    pushToast('error', `删除失败：${(error as Error).message}`)
  } finally {
    loadingPatternOps.value = false
  }
}

onMounted(() => {
  void bootstrapProjects()
})
</script>

<template>
  <section class="library-view">
    <header class="header">
      <h2>图解库</h2>
      <button class="create-btn" @click="showCreateModal = true">+ 新建项目</button>
    </header>

    <ErrorState
      v-if="loadError"
      title="项目加载失败"
      :description="loadError"
      action-text="重试"
      @retry="bootstrapProjects"
    />

    <p v-else-if="loadingProjects">正在加载项目...</p>

    <EmptyState v-else-if="!projectState.projects.length" title="还没有项目" description="请先创建项目后再添加图解。">
      <div style="margin-top: 10px">
        <button class="create-btn" @click="showCreateModal = true">立即创建</button>
      </div>
    </EmptyState>

    <ProjectCardGrid
      v-else
      :projects="projectState.projects"
      :active-project-id="activeProjectId"
      @manage="openPatternManager"
    />

    <CreateProjectModal
      :visible="showCreateModal"
      :loading="loadingCreate"
      @cancel="showCreateModal = false"
      @submit="handleCreateProject"
    />

    <PatternManagerDrawer
      :visible="showPatternDrawer"
      :project="selectedProject"
      :patterns="patternList"
      :current-pattern-id="selectedProject?.currentPatternId ?? null"
      :loading="loadingPatternOps"
      @close="showPatternDrawer = false"
      @upload="handleUploadPattern"
      @add-link="handleAddLink"
      @switch-current="handleSwitchCurrent"
      @remove="askDeletePattern"
    />

    <ConfirmDialog
      :visible="Boolean(deletingPattern)"
      title="删除图解"
      message="删除后无法恢复，确定继续吗？"
      @cancel="deletingPattern = null"
      @confirm="confirmDeletePattern"
    />
  </section>
</template>

<style scoped>
.library-view {
  display: grid;
  gap: 12px;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header h2 {
  margin: 0;
}

.create-btn {
  border: none;
  border-radius: 8px;
  background: #486f40;
  color: #fff;
  padding: 8px 12px;
  cursor: pointer;
}
</style>
