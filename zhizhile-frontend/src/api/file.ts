/**
 * 生成文件内容访问地址。
 * 后端该接口直接返回二进制内容，所以前端只需拼接 URL 给阅读组件使用。
 */
export function getFileContentUrl(fileId: number): string {
  const API_BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080/api'
  return `${API_BASE}/files/${fileId}/content`
}
