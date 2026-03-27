<script setup lang="ts">
import type { ProjectDetail } from '../../types/api'

/**
 * 项目卡片列表参数。
 */
const props = defineProps<{
  projects: ProjectDetail[]
  activeProjectId: number | null
}>()

const emit = defineEmits<{
  manage: [project: ProjectDetail]
  open: [project: ProjectDetail]
}>()
</script>

<template>
  <div class="grid">
    <article
      v-for="project in props.projects"
      :key="project.id"
      class="card"
      :class="{ active: props.activeProjectId === project.id }"
    >
      <div class="title">{{ project.name }}</div>
      <div class="meta">项目 ID：{{ project.id }}</div>
      <div class="actions">
        <button class="btn" @click="emit('manage', project)">管理图解</button>
        <button class="btn primary" @click="emit('open', project)">进入工作台</button>
      </div>
    </article>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  gap: 12px;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
}

.card {
  background: #fff;
  border: 1px solid #e0e6d8;
  border-radius: 12px;
  padding: 14px;
}

.card.active {
  border-color: #6a9360;
  box-shadow: 0 0 0 2px rgba(106, 147, 96, 0.18);
}

.title {
  font-weight: 700;
}

.meta {
  margin-top: 4px;
  color: #72806f;
  font-size: 12px;
}

.actions {
  margin-top: 10px;
  display: flex;
  gap: 8px;
}

.btn {
  border: none;
  border-radius: 8px;
  background: #edf2ea;
  padding: 7px 10px;
  cursor: pointer;
}

.btn.primary {
  background: #476f3e;
  color: #fff;
}
</style>
