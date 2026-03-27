<script setup lang="ts">
/**
 * 工作台控制卡参数。
 */
const props = defineProps<{
  autoWake: boolean
  enableSound: boolean
}>()

const emit = defineEmits<{
  updateAutoWake: [value: boolean]
  updateSound: [value: boolean]
  syncNow: []
}>()
</script>

<template>
  <section class="card">
    <h3>工作台控制</h3>

    <label class="row">
      <span>动作唤醒模式</span>
      <input type="checkbox" :checked="props.autoWake" @change="emit('updateAutoWake', ($event.target as HTMLInputElement).checked)" />
    </label>

    <label class="row">
      <span>计数音效</span>
      <input type="checkbox" :checked="props.enableSound" @change="emit('updateSound', ($event.target as HTMLInputElement).checked)" />
    </label>

    <button class="sync" @click="emit('syncNow')">立即同步</button>
  </section>
</template>

<style scoped>
.card {
  background: #fff;
  border: 1px solid #dfe6d8;
  border-radius: 12px;
  padding: 14px;
  display: grid;
  gap: 10px;
}

h3 {
  margin: 0;
}

.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.sync {
  border: none;
  border-radius: 8px;
  padding: 8px 10px;
  background: #476f3f;
  color: #fff;
  cursor: pointer;
}
</style>
