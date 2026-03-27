import { reactive } from 'vue'
import type { Pattern, ProjectDetail } from '../types/api'

/**
 * 用于本地记忆“近期访问项目”，帮助刷新后尽量恢复用户上下文。
 */
const RECENT_PROJECT_IDS_KEY = 'zzl_recent_project_ids'

/**
 * 项目域状态。
 */
export const projectState = reactive({
  projects: [] as ProjectDetail[],
  currentProject: null as ProjectDetail | null,
  patterns: [] as Pattern[],
  currentPattern: null as Pattern | null,
})

/**
 * 写入近期项目列表。
 */
export function saveRecentProjectId(projectId: number): void {
  const raw = localStorage.getItem(RECENT_PROJECT_IDS_KEY)
  const existing = raw ? (JSON.parse(raw) as number[]) : []
  const merged = [projectId, ...existing.filter((id) => id !== projectId)].slice(0, 20)
  localStorage.setItem(RECENT_PROJECT_IDS_KEY, JSON.stringify(merged))
}

/**
 * 读取近期项目 ID 列表。
 */
export function getRecentProjectIds(): number[] {
  const raw = localStorage.getItem(RECENT_PROJECT_IDS_KEY)
  if (!raw) {
    return []
  }

  try {
    return JSON.parse(raw) as number[]
  } catch {
    return []
  }
}
