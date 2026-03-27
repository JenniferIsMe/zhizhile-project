/**
 * 工作台快捷键回调。
 */
export interface ReaderHotkeyHandlers {
  onMoveUp: () => void
  onMoveDown: () => void
  onCount: () => void
}

/**
 * 判断当前焦点是否是输入控件。
 * 输入控件聚焦时需禁用快捷键，避免干扰打字。
 */
function isInputFocused(): boolean {
  const active = document.activeElement

  if (!active) {
    return false
  }

  return ['INPUT', 'TEXTAREA', 'SELECT'].includes(active.tagName) || active.hasAttribute('contenteditable')
}

/**
 * 绑定阅读器快捷键。
 * 返回卸载函数，用于页面销毁时释放监听。
 */
export function mountReaderHotkeys(handlers: ReaderHotkeyHandlers): () => void {
  const listener = (event: KeyboardEvent) => {
    if (isInputFocused()) {
      return
    }

    if (event.code === 'ArrowUp') {
      event.preventDefault()
      handlers.onMoveUp()
      return
    }

    if (event.code === 'ArrowDown') {
      event.preventDefault()
      handlers.onMoveDown()
      return
    }

    if (event.code === 'Space') {
      event.preventDefault()
      handlers.onCount()
    }
  }

  window.addEventListener('keydown', listener)

  return () => {
    window.removeEventListener('keydown', listener)
  }
}
