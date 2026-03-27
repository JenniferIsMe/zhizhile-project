<script setup lang="ts">
/**
 * 设置预览参数。
 */
const props = defineProps<{
  maskOpacity: number
}>()
</script>

<template>
  <section class="card">
    <h3>实时预览</h3>
    <div class="preview">
      <div class="mask" :style="{ background: `rgba(0,0,0,${props.maskOpacity})` }" />
      <div class="hole">Row 15: K2, K2, ...</div>
    </div>
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

.preview {
  position: relative;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  background: #f0f3ec;
}

.mask {
  position: absolute;
  inset: 0;
}

.hole {
  position: absolute;
  left: 12px;
  right: 12px;
  top: 42px;
  height: 34px;
  background: #fff;
  border-radius: 6px;
  display: grid;
  align-items: center;
  padding: 0 10px;
  color: #333;
}
</style>
