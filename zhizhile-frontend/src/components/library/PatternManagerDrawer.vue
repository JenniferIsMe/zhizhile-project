<script setup lang="ts">
import UploadPatternPanel from './UploadPatternPanel.vue'
import LinkPatternPanel from './LinkPatternPanel.vue'
import PatternList from './PatternList.vue'
import type { Pattern, ProjectDetail } from '../../types/api'

/**
 * 图解管理抽屉参数。
 */
const props = defineProps<{
  visible: boolean
  project: ProjectDetail | null
  patterns: Pattern[]
  currentPatternId: number | null
  loading: boolean
}>()

const emit = defineEmits<{
  close: []
  upload: [file: File]
  addLink: [url: string, displayName: string]
  switchCurrent: [pattern: Pattern]
  remove: [pattern: Pattern]
}>()
</script>

<template>
  <div v-if="props.visible" class="drawer-mask" @click.self="emit('close')">
    <aside class="drawer">
      <header class="head">
        <h3>图解管理</h3>
        <p>{{ props.project?.name ?? '未选择项目' }}</p>
      </header>

      <UploadPatternPanel :loading="props.loading" @upload="emit('upload', $event)" />
      <LinkPatternPanel :loading="props.loading" @submit="(url, displayName) => emit('addLink', url, displayName)" />

      <section class="list-wrap">
        <h4>图解列表</h4>
        <PatternList
          :patterns="props.patterns"
          :current-pattern-id="props.currentPatternId"
          @switch-current="emit('switchCurrent', $event)"
          @remove="emit('remove', $event)"
        />
      </section>

      <button class="close" @click="emit('close')">关闭</button>
    </aside>
  </div>
</template>

<style scoped>
.drawer-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 1600;
}

.drawer {
  margin-left: auto;
  width: 440px;
  height: 100%;
  background: #fff;
  padding: 14px;
  display: grid;
  grid-template-rows: auto auto auto 1fr auto;
  gap: 10px;
}

.head h3 {
  margin: 0;
}

.head p {
  margin: 4px 0 0;
  color: #6e7d6b;
}

.list-wrap {
  min-height: 0;
  overflow: auto;
}

.list-wrap h4 {
  margin: 0 0 8px;
}

.close {
  border: none;
  border-radius: 8px;
  background: #e8eee3;
  padding: 9px 10px;
  cursor: pointer;
}
</style>
