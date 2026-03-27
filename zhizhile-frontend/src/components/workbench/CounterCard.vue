<script setup lang="ts">
/**
 * 计数卡片参数。
 */
const props = defineProps<{
  currentRowIndex: number
  totalCount: number
}>()

const emit = defineEmits<{
  incrementCount: []
  changeRow: [value: number]
}>()

/**
 * 行号 +1。
 */
function increaseRow(): void {
  emit('changeRow', props.currentRowIndex + 1)
}

/**
 * 行号 -1，最低限制为 1。
 */
function decreaseRow(): void {
  emit('changeRow', Math.max(1, props.currentRowIndex - 1))
}

/**
 * 输入框调整行号。
 */
function handleInput(event: Event): void {
  const target = event.target as HTMLInputElement
  const value = Number(target.value)

  if (Number.isFinite(value) && value > 0) {
    emit('changeRow', Math.floor(value))
  }
}
</script>

<template>
  <section class="card">
    <h3>当前行数</h3>
    <div class="row-controls">
      <button class="tiny" @click="decreaseRow">-</button>
      <input type="number" :value="props.currentRowIndex" min="1" @change="handleInput" />
      <button class="tiny" @click="increaseRow">+</button>
    </div>

    <div class="count">累计计数：{{ props.totalCount }}</div>

    <button class="hot-area" @click="emit('incrementCount')">点击任意区域 +1</button>
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

.row-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.row-controls input {
  width: 90px;
  border: 1px solid #ccd7c4;
  border-radius: 6px;
  padding: 6px;
}

.tiny {
  border: none;
  border-radius: 6px;
  width: 28px;
  height: 28px;
  background: #edf1eb;
  cursor: pointer;
}

.count {
  font-size: 15px;
  color: #374934;
}

.hot-area {
  border: none;
  border-radius: 10px;
  height: 78px;
  background: #d8e8cf;
  color: #2e4d26;
  font-weight: 700;
  cursor: pointer;
}
</style>
