/**
 * 简单防抖工具。
 * 用于高频操作后的延迟提交，防止请求风暴。
 */
export function createDebounce<T extends unknown[]>(fn: (...args: T) => void, delay: number) {
  let timer = 0

  return (...args: T) => {
    window.clearTimeout(timer)
    timer = window.setTimeout(() => {
      fn(...args)
    }, delay)
  }
}
