import { request } from './http'
import type { Pattern } from '../types/api'

/**
 * 查询项目图解列表。
 */
export function listPatterns(projectId: number): Promise<Pattern[]> {
  return request<Pattern[]>(`/projects/${projectId}/patterns`)
}

/**
 * 上传图解文件。
 */
export function uploadPattern(projectId: number, file: File): Promise<Pattern> {
  const formData = new FormData()
  formData.append('file', file)

  return request<Pattern>(`/projects/${projectId}/patterns/upload`, {
    method: 'POST',
    body: formData,
  })
}

/**
 * 添加图解链接。
 */
export function createPatternLink(projectId: number, url: string, displayName: string): Promise<Pattern> {
  return request<Pattern>(`/projects/${projectId}/patterns/link`, {
    method: 'POST',
    body: JSON.stringify({ url, displayName }),
  })
}

/**
 * 手动切换当前图解。
 */
export function switchCurrentPattern(projectId: number, patternId: number): Promise<void> {
  return request<void>(`/projects/${projectId}/patterns/${patternId}/current`, {
    method: 'PUT',
  })
}

/**
 * 删除图解。
 */
export function deletePattern(projectId: number, patternId: number): Promise<void> {
  return request<void>(`/projects/${projectId}/patterns/${patternId}`, {
    method: 'DELETE',
  })
}
