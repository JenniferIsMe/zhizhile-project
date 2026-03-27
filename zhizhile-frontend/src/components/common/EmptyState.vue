<script setup lang="ts">
/**
 * 空态组件参数。
 */
const props = defineProps<{
  title: string
  description: string
}>()
</script>

<template>
  <div class="empty">
    <h3>{{ props.title }}</h3>
    <p>{{ props.description }}</p>
    <slot />
  </div>
</template>

<style scoped>
.empty {
  border: 1px dashed #cfd8c7;
  border-radius: 12px;
  padding: 22px;
  text-align: center;
  color: #5f6f5b;
  background: #f9fbf6;
}

h3 {
  margin: 0 0 8px;
}

p {
  margin: 0;
}
</style>
