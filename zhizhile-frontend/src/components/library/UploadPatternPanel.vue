<script setup lang="ts">
import { ref } from 'vue'

/**
 * 上传面板参数。
 */
const props = defineProps<{
  loading: boolean
}>()

const emit = defineEmits<{
  upload: [file: File]
}>()

/**
 * 本地选择文件状态。
 */
const selectedFile = ref<File | null>(null)

/**
 * 接收 input 文件选择结果。
 */
function handleFileChange(event: Event): void {
  const target = event.target as HTMLInputElement
  selectedFile.value = target.files?.[0] ?? null
}

/**
 * 触发上传。
 */
function handleUpload(): void {
  if (!selectedFile.value) {
    return
  }

  emit('upload', selectedFile.value)
}
</script>

<template>
  <div class="panel">
    <h4>上传图解</h4>
    <input type="file" accept=".pdf,image/*" @change="handleFileChange" />
    <button class="btn" :disabled="!selectedFile || props.loading" @click="handleUpload">
      {{ props.loading ? '上传中...' : '开始上传' }}
    </button>
  </div>
</template>

<style scoped>
.panel {
  border: 1px solid #e1e7da;
  border-radius: 10px;
  padding: 10px;
  display: grid;
  gap: 8px;
}

h4 {
  margin: 0;
}

.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 10px;
  background: #4d7642;
  color: #fff;
  cursor: pointer;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
