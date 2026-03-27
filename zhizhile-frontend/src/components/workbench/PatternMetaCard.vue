<script setup lang="ts">
import type { Pattern, ProjectDetail } from '../../types/api'

/**
 * 项目与图解元信息参数。
 */
const props = defineProps<{
  project: ProjectDetail | null
  pattern: Pattern | null
}>()
</script>

<template>
  <section class="meta-card">
    <h3>当前项目</h3>
    <p>{{ props.project?.name ?? '-' }}</p>
    <h3>当前图解</h3>
    <p>{{ props.pattern?.displayName ?? '未选择图解' }}</p>
  </section>
</template>

<style scoped>
.meta-card {
  background: #fff;
  border: 1px solid #dee5d7;
  border-radius: 12px;
  padding: 14px;
}

h3 {
  margin: 0 0 6px;
  font-size: 14px;
}

p {
  margin: 0 0 10px;
  color: #495a46;
}
</style>
