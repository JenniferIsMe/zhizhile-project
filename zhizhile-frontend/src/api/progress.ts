import { request } from './http'
import type { ProjectProgress } from '../types/api'

/**
 * 查询项目进度。
 */
export function getProgress(projectId: number): Promise<ProjectProgress> {
  return request<ProjectProgress>(`/projects/${projectId}/progress`)
}

/**
 * 保存计数进度。
 */
export function saveRowProgress(projectId: number, currentRowIndex: number, totalCount: number): Promise<ProjectProgress> {
  return request<ProjectProgress>(`/projects/${projectId}/progress/row`, {
    method: 'PUT',
    body: JSON.stringify({ currentRowIndex, totalCount }),
  })
}

/**
 * 保存计时进度。
 */
export function saveTimeProgress(projectId: number, totalSeconds: number): Promise<ProjectProgress> {
  return request<ProjectProgress>(`/projects/${projectId}/progress/time`, {
    method: 'PUT',
    body: JSON.stringify({ totalSeconds }),
  })
}
