<script setup lang="ts">
import { ref } from 'vue'

/**
 * 链接图解面板参数。
 */
const props = defineProps<{
  loading: boolean
}>()

const emit = defineEmits<{
  submit: [url: string, displayName: string]
}>()

const url = ref('')
const displayName = ref('')
const error = ref('')

/**
 * URL 预校验。
 */
function isValidUrl(value: string): boolean {
  try {
    const parsed = new URL(value)
    return parsed.protocol === 'http:' || parsed.protocol === 'https:'
  } catch {
    return false
  }
}

/**
 * 提交链接图解。
 */
function handleSubmit(): void {
  if (!isValidUrl(url.value.trim())) {
    error.value = '请输入合法的 http/https 链接'
    return
  }

  error.value = ''
  emit('submit', url.value.trim(), displayName.value.trim() || '链接图解')
}
</script>

<template>
  <div class="panel">
    <h4>添加链接图解</h4>
    <input v-model="displayName" placeholder="图解名称（可选）" />
    <input v-model="url" placeholder="https://example.com/pattern" />
    <p v-if="error" class="error">{{ error }}</p>
    <button class="btn" :disabled="props.loading" @click="handleSubmit">
      {{ props.loading ? '提交中...' : '添加链接' }}
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

input {
  border: 1px solid #d4dccf;
  border-radius: 8px;
  padding: 8px 10px;
}

.error {
  margin: 0;
  color: #962f2f;
  font-size: 12px;
}

.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 10px;
  background: #4d7642;
  color: #fff;
  cursor: pointer;
}
</style>
