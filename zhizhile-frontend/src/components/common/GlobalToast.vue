<script setup lang="ts">
import { toastState } from '../../stores/toast'
</script>

<template>
  <div class="toast-wrap">
    <div
      v-for="item in toastState.items"
      :key="item.id"
      class="toast"
      :class="item.type"
    >
      {{ item.text }}
    </div>
  </div>
</template>

<style scoped>
.toast-wrap {
  position: fixed;
  right: 20px;
  top: 20px;
  display: grid;
  gap: 8px;
  z-index: 9999;
}

.toast {
  border-radius: 10px;
  padding: 10px 12px;
  min-width: 220px;
  color: #fff;
  font-size: 13px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.toast.success {
  background: #3f6b33;
}

.toast.error {
  background: #9b2f2f;
}

.toast.info {
  background: #5a6470;
}
</style>
