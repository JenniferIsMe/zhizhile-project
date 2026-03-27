<script setup lang="ts">
/**
 * 二次确认弹窗参数。
 */
const props = defineProps<{
  visible: boolean
  title: string
  message: string
}>()

const emit = defineEmits<{
  cancel: []
  confirm: []
}>()
</script>

<template>
  <div v-if="props.visible" class="mask" @click.self="emit('cancel')">
    <div class="panel">
      <h3>{{ props.title }}</h3>
      <p>{{ props.message }}</p>
      <div class="actions">
        <button class="btn ghost" @click="emit('cancel')">取消</button>
        <button class="btn danger" @click="emit('confirm')">确认</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.35);
  display: grid;
  place-items: center;
  z-index: 2000;
}

.panel {
  width: 360px;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}

.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.btn.ghost {
  background: #edf1eb;
}

.btn.danger {
  background: #8f2c2c;
  color: #fff;
}
</style>
