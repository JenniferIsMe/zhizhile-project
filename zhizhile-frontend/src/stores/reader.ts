import { reactive } from 'vue'

/**
 * 阅读状态域。
 * 由工作台多个组件共享。
 */
export const readerState = reactive({
  currentPage: 1,
  zoom: 1,
  maskTopOffset: 120,
  maskHeight: 48,
  saveStatus: 'idle' as 'idle' | 'saving' | 'saved' | 'error',
})
