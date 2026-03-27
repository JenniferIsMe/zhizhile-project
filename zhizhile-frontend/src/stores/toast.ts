import { reactive } from 'vue'

/**
 * Toast 消息项。
 */
export interface ToastItem {
  id: number
  type: 'success' | 'error' | 'info'
  text: string
}

/**
 * 全局 toast 状态。
 */
export const toastState = reactive({
  items: [] as ToastItem[],
})

let seed = 1

/**
 * 推送一条消息。
 */
export function pushToast(type: ToastItem['type'], text: string): void {
  const id = seed++
  toastState.items.push({ id, type, text })

  window.setTimeout(() => {
    const index = toastState.items.findIndex((item) => item.id === id)
    if (index >= 0) {
      toastState.items.splice(index, 1)
    }
  }, 2800)
}
