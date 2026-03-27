<script setup lang="ts">
import { ref, watch } from 'vue'

/**
 * 创建项目弹窗参数。
 */
const props = defineProps<{
  visible: boolean
  loading: boolean
}>()

const emit = defineEmits<{
  cancel: []
  submit: [name: string]
}>()

/**
 * 项目名输入状态。
 */
const name = ref('')
const error = ref('')

/**
 * 每次弹窗打开时清空历史输入，避免脏数据残留。
 */
watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      name.value = ''
      error.value = ''
    }
  },
)

/**
 * 提交前做必填校验。
 */
function handleSubmit(): void {
  if (!name.value.trim()) {
    error.value = '项目名称不能为空'
    return
  }

  emit('submit', name.value.trim())
}
</script>

<template>
  <div v-if="props.visible" class="mask" @click.self="emit('cancel')">
    <div class="modal">
      <h3>新建项目</h3>
      <input v-model="name" placeholder="请输入项目名称" />
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
  width: 360px;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
}

input {
  width: 100%;
  margin-top: 8px;
  border: 1px solid #d2dccc;
  border-radius: 8px;
  padding: 9px 10px;
}

.error {
  color: #962f2f;
  margin: 6px 0 0;
  font-size: 12px;
}

.actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 10px;
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
