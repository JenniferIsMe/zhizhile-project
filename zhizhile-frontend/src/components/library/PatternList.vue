<script setup lang="ts">
import type { Pattern } from '../../types/api'

/**
 * 图解列表参数。
 */
const props = defineProps<{
  patterns: Pattern[]
  currentPatternId: number | null
}>()

const emit = defineEmits<{
  switchCurrent: [pattern: Pattern]
  remove: [pattern: Pattern]
}>()
</script>

<template>
  <div class="list">
    <article v-for="pattern in props.patterns" :key="pattern.id" class="item">
      <div>
        <div class="name">{{ pattern.displayName }}</div>
        <div class="meta">{{ pattern.sourceType }} / ID {{ pattern.id }}</div>
      </div>
      <div class="actions">
        <button
          class="btn"
          :disabled="props.currentPatternId === pattern.id"
          @click="emit('switchCurrent', pattern)"
        >
          {{ props.currentPatternId === pattern.id ? '当前图解' : '设为当前' }}
        </button>
        <button class="btn danger" @click="emit('remove', pattern)">删除</button>
      </div>
    </article>
  </div>
</template>

<style scoped>
.list {
  display: grid;
  gap: 8px;
}

.item {
  border: 1px solid #e0e6d8;
  border-radius: 10px;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  gap: 8px;
}

.name {
  font-weight: 600;
}

.meta {
  font-size: 12px;
  color: #748271;
}

.actions {
  display: flex;
  gap: 6px;
}

.btn {
  border: none;
  border-radius: 7px;
  background: #ecf2e8;
  padding: 6px 9px;
  cursor: pointer;
}

.btn.danger {
  background: #f5dcdc;
  color: #842f2f;
}
</style>
