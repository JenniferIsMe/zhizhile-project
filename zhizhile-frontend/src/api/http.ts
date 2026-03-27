import type { ApiResponse } from '../types/api'

/**
 * API 根路径。
 * 默认指向本机后端服务，可通过 Vite 环境变量覆盖。
 */
const API_BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080/api'

/**
 * 自定义业务异常。
 * 统一承载后端返回的 code/message，方便页面层做精细提示。
 */
export class BizError extends Error {
  code: string

  constructor(code: string, message: string) {
    super(message)
    this.code = code
  }
}

/**
 * 通用请求方法。
 * 负责：
 * 1) 自动拼接 base URL
 * 2) 统一请求头
 * 3) 统一解包 ApiResponse
 * 4) 统一错误抛出
 */
export async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      ...(init?.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }),
      ...init?.headers,
    },
    ...init,
  })

  if (!response.ok) {
    throw new BizError('HTTP_ERROR', `请求失败：${response.status}`)
  }

  const data = (await response.json()) as ApiResponse<T>

  if (!data.success) {
    throw new BizError(data.code, data.message)
  }

  return data.data
}
