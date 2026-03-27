<script setup lang="ts">
/**
 * 聚焦窗设置参数。
 */
const props = defineProps<{
  maskStep: number
  maskOpacity: number
}>()

const emit = defineEmits<{
  updateMaskStep: [value: number]
  updateMaskOpacity: [value: number]
}>()
</script>

<template>
  <section class="card">
    <h3>聚焦视窗</h3>

    <label class="row">
      <span>步进大小：{{ props.maskStep }}px</span>
      <input
        type="range"
        min="12"
        max="48"
        :value="props.maskStep"
        @input="emit('updateMaskStep', Number(($event.target as HTMLInputElement).value))"
      />
    </label>

    <label class="row">
      <span>遮罩透明度：{{ Math.round(props.maskOpacity * 100) }}%</span>
      <input
        type="range"
        min="0.1"
        max="0.8"
        step="0.05"
        :value="props.maskOpacity"
        @input="emit('updateMaskOpacity', Number(($event.target as HTMLInputElement).value))"
      />
    </label>
  </section>
</template>

<style scoped>
.card {
  background: #fff;
  border: 1px solid #dfe6d8;
  border-radius: 12px;
  padding: 14px;
  display: grid;
  gap: 12px;
}

h3 {
  margin: 0;
}

.row {
  display: grid;
  gap: 8px;
}
</style>
