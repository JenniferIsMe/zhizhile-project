import { request } from './http'
import type { ProjectDetail, ProjectOverview } from '../types/api'

/**
 * 创建项目。
 * type: 0=钩针 1=棒针
 */
export function createProject(name: string, type: number): Promise<ProjectDetail> {
  return request<ProjectDetail>('/projects', {
    method: 'POST',
    body: JSON.stringify({ name, type }),
  })
}

/**
 * 查询项目详情。
 */
export function getProjectDetail(projectId: number): Promise<ProjectDetail> {
  return request<ProjectDetail>(`/projects/${projectId}`)
}

/**
 * 查询项目总览。
 */
export function getProjectOverview(projectId: number): Promise<ProjectOverview> {
  return request<ProjectOverview>(`/projects/${projectId}/overview`)
}
