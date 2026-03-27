import { computed, ref } from 'vue'

/**
 * 路由名称集合。
 * 为了避免字符串散落在各个组件中，统一在这里集中声明。
 */
export type RouteName = 'library' | 'workbench' | 'settings'

/**
 * 路由定义。
 */
interface RouteRecord {
  name: RouteName
  path: string
}

/**
 * 应用路由表。
 */
const routeTable: RouteRecord[] = [
  { name: 'library', path: '/library' },
  { name: 'workbench', path: '/workbench/:projectId' },
  { name: 'settings', path: '/settings' },
]

/**
 * 当前浏览器路径。
 * 使用 ref 让所有消费方可以响应式感知地址变化。
 */
const currentPath = ref(window.location.pathname || '/library')

/**
 * 将动态路径模板转换为可匹配正则。
 */
function buildMatcher(path: string): RegExp {
  const pattern = path.replace(/:[^/]+/g, '([^/]+)')
  return new RegExp(`^${pattern}$`)
}

/**
 * 将动态路径中的参数名抽取出来。
 */
function extractParamNames(path: string): string[] {
  const names: string[] = []
  const matches = path.match(/:[^/]+/g) ?? []
  matches.forEach((segment) => {
    names.push(segment.replace(':', ''))
  })
  return names
}

/**
 * 解析当前路径，拿到路由名和 params。
 */
function resolve(pathname: string): { name: RouteName; params: Record<string, string> } {
  const matched = routeTable.find((route) => buildMatcher(route.path).test(pathname))

  if (!matched) {
    return { name: 'library', params: {} }
  }

  const matcher = buildMatcher(matched.path)
  const values = pathname.match(matcher)?.slice(1) ?? []
  const keys = extractParamNames(matched.path)
  const params: Record<string, string> = {}

  keys.forEach((key, index) => {
    params[key] = values[index] ?? ''
  })

  return { name: matched.name, params }
}

/**
 * 执行跳转。
 * 这里使用 history.pushState 保持 URL 变化且不触发整页刷新。
 */
export function navigate(path: string): void {
  window.history.pushState({}, '', path)
  currentPath.value = path
}

/**
 * 全局 popstate 监听。
 * 支持浏览器后退/前进时自动更新当前路径。
 */
window.addEventListener('popstate', () => {
  currentPath.value = window.location.pathname || '/library'
})

/**
 * 路由状态钩子。
 * 提供当前路由名、参数和路径给页面层使用。
 */
export function useRouterState() {
  const resolved = computed(() => resolve(currentPath.value))

  return {
    path: currentPath,
    routeName: computed(() => resolved.value.name),
    params: computed(() => resolved.value.params),
  }
}
