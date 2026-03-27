<script setup lang="ts">
/**
 * 错误态参数。
 */
const props = defineProps<{
  title: string
  description: string
  actionText?: string
}>()

const emit = defineEmits<{
  retry: []
}>()
</script>

<template>
  <div class="error-state">
    <h3>{{ props.title }}</h3>
    <p>{{ props.description }}</p>
    <button v-if="props.actionText" class="retry" @click="emit('retry')">{{ props.actionText }}</button>
  </div>
</template>

<style scoped>
.error-state {
  border: 1px solid #f0c8c8;
  background: #fff6f6;
  border-radius: 12px;
  padding: 16px;
  color: #7a2f2f;
}

.retry {
  margin-top: 8px;
  border: none;
  border-radius: 8px;
  background: #7a2f2f;
  color: #fff;
  padding: 7px 10px;
  cursor: pointer;
}
</style>
