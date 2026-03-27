<script setup lang="ts">
import { computed } from 'vue'

/**
 * 聚焦遮罩参数。
 */
const props = defineProps<{
  maskTopOffset: number
  maskHeight: number
  opacity: number
}>()

/**
 * 通过 box-shadow 方式创建“镂空视窗”效果。
 * pointer-events: none 用于保证遮罩不会拦截滚动和点击。
 */
const holeStyle = computed(() => ({
  top: `${props.maskTopOffset}px`,
  height: `${props.maskHeight}px`,
  boxShadow: `0 0 0 99999px rgba(0,0,0,${props.opacity})`,
}))
</script>

<template>
  <div class="overlay">
    <div class="hole" :style="holeStyle" />
  </div>
</template>

<style scoped>
.overlay {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.hole {
  position: absolute;
  left: 0;
  right: 0;
  border-top: 1px solid rgba(255, 255, 255, 0.45);
  border-bottom: 1px solid rgba(255, 255, 255, 0.45);
}
</style>
