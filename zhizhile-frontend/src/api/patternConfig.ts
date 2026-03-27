import { request } from './http'
import type { PatternConfig } from '../types/api'

/**
 * 查询阅读配置。
 */
export function getPatternConfig(projectId: number, patternId: number): Promise<PatternConfig> {
  return request<PatternConfig>(`/projects/${projectId}/patterns/${patternId}/config`)
}

/**
 * 保存阅读配置。
 */
export function savePatternConfig(
  projectId: number,
  patternId: number,
  payload: Pick<PatternConfig, 'currentPage' | 'maskTopOffset' | 'maskHeight'>,
): Promise<PatternConfig> {
  return request<PatternConfig>(`/projects/${projectId}/patterns/${patternId}/config`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}
