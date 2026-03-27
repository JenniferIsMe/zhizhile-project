<script setup lang="ts">
/**
 * 保存状态参数。
 */
const props = defineProps<{
  status: 'idle' | 'saving' | 'saved' | 'error'
}>()

/**
 * 状态文案映射。
 */
type SaveStatus = 'idle' | 'saving' | 'saved' | 'error'

const textMap: Record<SaveStatus, string> = {
  idle: '等待同步',
  saving: '同步中...',
  saved: '已同步',
  error: '同步失败',
}
</script>

<template>
  <div class="indicator" :class="props.status">{{ textMap[props.status] }}</div>
</template>

<style scoped>
.indicator {
  border-radius: 8px;
  padding: 8px 10px;
  font-size: 12px;
  width: fit-content;
}

.indicator.idle {
  background: #ecefea;
}

.indicator.saving {
  background: #fff3cd;
  color: #6b5700;
}

.indicator.saved {
  background: #dff2dc;
  color: #28562a;
}

.indicator.error {
  background: #f8dddd;
  color: #7a2525;
}
</style>
