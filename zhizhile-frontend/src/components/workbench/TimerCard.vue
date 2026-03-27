<script setup lang="ts">
import { computed } from 'vue'

/**
 * 计时卡片参数。
 */
const props = defineProps<{
  totalSeconds: number
  running: boolean
  autoWake: boolean
}>()

const emit = defineEmits<{
  toggle: []
}>()

/**
 * 秒转 HH:mm:ss 展示。
 */
const formatted = computed(() => {
  const hours = Math.floor(props.totalSeconds / 3600)
  const minutes = Math.floor((props.totalSeconds % 3600) / 60)
  const seconds = props.totalSeconds % 60

  return [hours, minutes, seconds].map((value) => String(value).padStart(2, '0')).join(':')
})
</script>

<template>
  <section class="card">
    <h3>编织计时器</h3>
    <div class="mode">模式：{{ props.autoWake ? '动作唤醒' : '手动' }}</div>
    <div class="time">{{ formatted }}</div>
    <button class="toggle" @click="emit('toggle')">{{ props.running ? '暂停' : '开始' }}</button>
  </section>
</template>

<style scoped>
.card {
  background: #fff;
  border: 1px solid #dee5d7;
  border-radius: 12px;
  padding: 14px;
  display: grid;
  gap: 10px;
}

h3 {
  margin: 0;
}

.mode {
  font-size: 13px;
  color: #6a7a66;
}

.time {
  font-size: 34px;
  font-weight: 700;
  color: #283528;
}

.toggle {
  border: none;
  border-radius: 8px;
  padding: 8px 10px;
  background: #466f3f;
  color: #fff;
  cursor: pointer;
}
</style>
