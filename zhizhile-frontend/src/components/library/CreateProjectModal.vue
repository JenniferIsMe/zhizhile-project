<script setup lang="ts">
import { ref, watch } from 'vue'
import type { PatternSourceInput } from '../../types/api'

/**
 * 创建项目弹窗参数。
 */
const props = defineProps<{
  visible: boolean
  loading: boolean
}>()

const emit = defineEmits<{
  cancel: []
  submit: [name: string, type: number, patternSource: PatternSourceInput]
}>()

/** 项目基本信息 */
const name = ref('')
/** 项目类型：1=棒针 0=钩针，默认棒针 */
const projectType = ref<number>(1)

/** 图解来源模式 */
const patternMode = ref<'upload' | 'link'>('upload')

/** 上传模式 */
const selectedFile = ref<File | null>(null)

/** 链接模式 */
const linkUrl = ref('')
const linkName = ref('')

const error = ref('')

/**
 * 每次弹窗打开时清空历史输入，避免脏数据残留。
 */
watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      name.value = ''
      projectType.value = 1
      patternMode.value = 'upload'
      selectedFile.value = null
      linkUrl.value = ''
      linkName.value = ''
      error.value = ''
    }
  },
)

function handleFileChange(event: Event): void {
  const target = event.target as HTMLInputElement
  selectedFile.value = target.files?.[0] ?? null
}

function isValidUrl(value: string): boolean {
  try {
    const parsed = new URL(value)
    return parsed.protocol === 'http:' || parsed.protocol === 'https:'
  } catch {
    return false
  }
}

/**
 * 提交前做必填校验，通过后 emit 项目名、类型、图解来源三元信息。
 */
function handleSubmit(): void {
  if (!name.value.trim()) {
    error.value = '项目名称不能为空'
    return
  }

  if (patternMode.value === 'upload') {
    if (!selectedFile.value) {
      error.value = '请选择要上传的图解文件'
      return
    }
    error.value = ''
    emit('submit', name.value.trim(), projectType.value, {
      kind: 'upload',
      file: selectedFile.value,
    })
  } else {
    if (!isValidUrl(linkUrl.value.trim())) {
      error.value = '请输入合法的 http/https 链接'
      return
    }
    error.value = ''
    emit('submit', name.value.trim(), projectType.value, {
      kind: 'link',
      url: linkUrl.value.trim(),
      displayName: linkName.value.trim() || '链接图解',
    })
  }
}
</script>

<template>
  <div v-if="props.visible" class="mask" @click.self="emit('cancel')">
    <div class="modal">
      <h3>新建项目</h3>

      <!-- 项目名称 -->
      <input v-model="name" placeholder="请输入项目名称" class="name-input" />

      <!-- 项目类型 -->
      <div class="field">
        <span class="field-label">项目类型</span>
        <div class="radio-group">
          <label class="radio-item">
            <input type="radio" :value="1" v-model="projectType" />
            棒针
          </label>
          <label class="radio-item">
            <input type="radio" :value="0" v-model="projectType" />
            钩针
          </label>
        </div>
      </div>

      <!-- 图解来源 -->
      <div class="field">
        <span class="field-label">图解</span>
        <div class="tab-bar">
          <button
            type="button"
            :class="['tab-btn', { active: patternMode === 'upload' }]"
            @click="patternMode = 'upload'"
          >
            上传图片
          </button>
          <button
            type="button"
            :class="['tab-btn', { active: patternMode === 'link' }]"
            @click="patternMode = 'link'"
          >
            链接
          </button>
        </div>

        <!-- 上传分支 -->
        <div v-if="patternMode === 'upload'" class="source-panel">
          <input type="file" accept=".pdf,image/*" @change="handleFileChange" />
          <p v-if="selectedFile" class="hint">已选：{{ selectedFile.name }}</p>
        </div>

        <!-- 链接分支 -->
        <div v-else class="source-panel">
          <input v-model="linkName" placeholder="图解名称（可选）" />
          <input v-model="linkUrl" placeholder="https://example.com/pattern" />
        </div>
      </div>

      <p v-if="error" class="error">{{ error }}</p>

      <div class="actions">
        <button class="btn ghost" @click="emit('cancel')">取消</button>
        <button class="btn primary" :disabled="props.loading" @click="handleSubmit">
          {{ props.loading ? '创建中...' : '创建项目' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.35);
  display: grid;
  place-items: center;
  z-index: 2000;
}

.modal {
  width: 400px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: grid;
  gap: 12px;
}

h3 {
  margin: 0;
}

.name-input {
  width: 100%;
  border: 1px solid #d2dccc;
  border-radius: 8px;
  padding: 9px 10px;
  box-sizing: border-box;
}

.field {
  display: grid;
  gap: 8px;
}

.field-label {
  font-size: 13px;
  font-weight: 500;
  color: #444;
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  font-size: 14px;
}

.tab-bar {
  display: flex;
  gap: 0;
  border: 1px solid #d2dccc;
  border-radius: 8px;
  overflow: hidden;
}

.tab-btn {
  flex: 1;
  padding: 7px 0;
  border: none;
  background: #f5f7f4;
  cursor: pointer;
  font-size: 13px;
  transition: background 0.15s;
}

.tab-btn.active {
  background: #3e6f34;
  color: #fff;
}

.source-panel {
  display: grid;
  gap: 8px;
}

.source-panel input[type='text'],
.source-panel input:not([type]) {
  border: 1px solid #d2dccc;
  border-radius: 8px;
  padding: 8px 10px;
}

.hint {
  margin: 0;
  font-size: 12px;
  color: #666;
}

.error {
  color: #962f2f;
  margin: 0;
  font-size: 12px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.btn.primary {
  background: #3e6f34;
  color: #fff;
}

.btn.ghost {
  background: #edf1eb;
}
</style>
