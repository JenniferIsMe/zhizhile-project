<script setup lang="ts">
import { computed } from 'vue'

/**
 * 阅读器参数。
 */
const props = defineProps<{
  sourceUrl: string
  sourceType: 'pdf' | 'image' | 'external' | 'unknown'
  currentPage: number
  zoom: number
}>()

const emit = defineEmits<{
  updatePage: [page: number]
  zoomIn: []
  zoomOut: []
}>()

/**
 * 当前仅提供页码输入能力。
 * PDF 的真实分页解析成本较高，MVP 阶段先通过用户可控输入同步页码。
 */
const zoomStyle = computed(() => ({
  transform: `scale(${props.zoom})`,
  transformOrigin: 'top left',
}))

/**
 * 页码输入事件处理。
 */
function handlePageChange(event: Event): void {
  const target = event.target as HTMLInputElement
  const page = Number(target.value)

  if (Number.isFinite(page) && page > 0) {
    emit('updatePage', Math.floor(page))
  }
}
</script>

<template>
  <section class="viewport-wrap">
    <header class="toolbar">
      <button class="tool" @click="emit('zoomOut')">-</button>
      <button class="tool" @click="emit('zoomIn')">+</button>
      <label class="page-box">
        当前页
        <input type="number" min="1" :value="props.currentPage" @change="handlePageChange" />
      </label>
    </header>

    <div class="viewport">
      <iframe
        v-if="props.sourceType === 'pdf'"
        :src="props.sourceUrl"
        class="pdf-frame"
        :style="zoomStyle"
        title="pattern-pdf"
      />

      <img v-else-if="props.sourceType === 'image'" :src="props.sourceUrl" class="image" :style="zoomStyle" alt="图解" />

      <iframe
        v-else-if="props.sourceType === 'external'"
        :src="props.sourceUrl"
        class="pdf-frame"
        title="pattern-external"
      />

      <p v-else class="unsupported">暂不支持当前图解类型</p>
    </div>
  </section>
</template>

<style scoped>
.viewport-wrap {
  height: 100%;
  display: grid;
  grid-template-rows: auto 1fr;
  background: #eef1ea;
  border-radius: 12px;
  border: 1px solid #dfe5d8;
  overflow: hidden;
}

.toolbar {
  background: #f9fbf7;
  border-bottom: 1px solid #dde4d5;
  padding: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.tool {
  border: none;
  border-radius: 7px;
  background: #e7eee1;
  width: 30px;
  height: 30px;
  cursor: pointer;
}

.page-box {
  margin-left: auto;
  font-size: 13px;
  color: #566655;
  display: flex;
  align-items: center;
  gap: 6px;
}

.page-box input {
  width: 70px;
  border: 1px solid #ccd6c6;
  border-radius: 6px;
  padding: 5px;
}

.viewport {
  overflow: auto;
  display: grid;
  place-items: start center;
  padding: 14px;
  background: #dde2d8;
}

.pdf-frame {
  width: 820px;
  height: 900px;
  border: none;
  background: #fff;
}

.image {
  max-width: none;
  width: 820px;
  display: block;
  background: #fff;
}

.unsupported {
  color: #6d7a6a;
}
</style>
