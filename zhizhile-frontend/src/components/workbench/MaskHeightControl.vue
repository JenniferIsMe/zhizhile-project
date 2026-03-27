<script setup lang="ts">
/**
 * 视窗高度调节参数。
 */
const props = defineProps<{
  value: number
  min?: number
  max?: number
}>()

const emit = defineEmits<{
  change: [value: number]
}>()

/**
 * 处理滑杆值变化。
 */
function handleInput(event: Event): void {
  const target = event.target as HTMLInputElement
  emit('change', Number(target.value))
}
</script>

<template>
  <label class="control">
    聚焦窗高度：{{ props.value }}px
    <input type="range" :value="props.value" :min="props.min ?? 24" :max="props.max ?? 220" @input="handleInput" />
  </label>
</template>

<style scoped>
.control {
  display: grid;
  gap: 8px;
  font-size: 13px;
  color: #5f6d5b;
}
</style>
