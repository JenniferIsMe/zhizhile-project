/**
 * 后端统一响应结构。
 * 所有 API 请求都会先经过该结构解包，再返回具体 data。
 */
export interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

/**
 * 项目详情。
 */
export interface ProjectDetail {
  id: number
  name: string
  currentPatternId: number | null
}

/**
 * 图解来源类型。
 */
export type PatternSourceType = 'UPLOAD' | 'LINK' | string

/**
 * 图解数据结构。
 */
export interface Pattern {
  id: number
  projectId: number
  sourceType: PatternSourceType
  displayName: string
  fileId: number | null
  externalUrl: string | null
}

/**
 * 阅读配置结构。
 */
export interface PatternConfig {
  id?: number
  projectId: number
  patternId: number
  currentPage: number
  maskTopOffset: number
  maskHeight: number
  updateTime?: string
}

/**
 * 进度结构。
 */
export interface ProjectProgress {
  id?: number
  projectId: number
  currentRowIndex: number
  totalCount: number
  totalSeconds: number
  updateTime?: string
}

/**
 * 项目总览结构。
 */
export interface ProjectOverview {
  project: ProjectDetail
  currentPattern: Pattern | null
  patterns: Pattern[]
  patternConfig: PatternConfig | null
  progress: ProjectProgress | null
}
