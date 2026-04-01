<script setup lang="ts">
import { useRouterState, navigate } from '../../router'
import { getRecentProjectIds, projectState } from '../../stores/project'

/**
 * 侧边栏导航数据。
 * 按原型补齐图解库、工作台、数据统计、设置四个入口。
 */
const navItems = [
  { key: 'library', label: '图解库', path: '/library', icon: 'book' },
  { key: 'workbench', label: '工作台', path: '/workbench', icon: 'compass' },
  { key: 'stats', label: '数据统计', path: '/stats', icon: 'chart' },
  { key: 'settings', label: '设置', path: '/settings', icon: 'gear' },
] as const

const router = useRouterState()

/**
 * 处理导航点击。
 */
function handleClick(path: string): void {
  /**
   * 工作台入口需要 projectId。
   * 优先使用当前项目，其次回退到最近访问项目；都没有时回到图解库。
   */
  if (path === '/workbench') {
    const currentProjectId = projectState.currentProject?.id
    const recentProjectId = getRecentProjectIds()[0]
    const targetProjectId = currentProjectId ?? recentProjectId

    if (!targetProjectId) {
      navigate('/library')
      return
    }

    navigate(`/workbench/${targetProjectId}`)
    return
  }

  navigate(path)
}
</script>

<template>
  <aside class="side-nav">
    <div class="brand-wrap">
      <div class="brand">织织乐</div>
      <div class="slogan">编织时光，慢生活手作</div>
    </div>

    <button
      v-for="item in navItems"
      :key="item.key"
      class="nav-item"
      :class="{ active: router.routeName.value === item.key }"
      @click="handleClick(item.path)"
    >
      <span class="icon-wrap">
        <svg v-if="item.icon === 'book'" viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M3 5.5A2.5 2.5 0 0 1 5.5 3H11v16H5.5A2.5 2.5 0 0 0 3 21V5.5Z" />
          <path d="M21 5.5A2.5 2.5 0 0 0 18.5 3H13v16h5.5A2.5 2.5 0 0 1 21 21V5.5Z" />
        </svg>

        <svg v-else-if="item.icon === 'compass'" viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="6" r="2" />
          <path d="M12 8v3" />
          <path d="M12 11l-4 9" />
          <path d="M12 11l4 9" />
        </svg>

        <svg v-else-if="item.icon === 'chart'" viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="11" width="5" height="10" />
          <rect x="10" y="7" width="5" height="14" />
          <rect x="17" y="14" width="4" height="7" />
        </svg>

        <svg v-else viewBox="0 0 24 24" class="icon" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="3.5" />
          <path d="M19.4 15a1.7 1.7 0 0 0 .34 1.87l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06A1.7 1.7 0 0 0 15 19.4a1.7 1.7 0 0 0-1 .9 1.7 1.7 0 0 0-.13.7V21a2 2 0 1 1-4 0v-.1a1.7 1.7 0 0 0-1.13-1.6 1.7 1.7 0 0 0-1.87.34l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.7 1.7 0 0 0 4.6 15a1.7 1.7 0 0 0-.9-1 1.7 1.7 0 0 0-.7-.13H3a2 2 0 1 1 0-4h.1a1.7 1.7 0 0 0 1.6-1.13 1.7 1.7 0 0 0-.34-1.87l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.7 1.7 0 0 0 9 4.6a1.7 1.7 0 0 0 1-.9 1.7 1.7 0 0 0 .13-.7V3a2 2 0 1 1 4 0v.1a1.7 1.7 0 0 0 1.13 1.6 1.7 1.7 0 0 0 1.87-.34l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.7 1.7 0 0 0 19.4 9c.42.2.74.56.9 1 .08.23.12.46.13.7V11a2 2 0 1 1 0 4h-.1a1.7 1.7 0 0 0-1.6 1.13Z" />
        </svg>
      </span>
      {{ item.label }}
    </button>
  </aside>
</template>

<style scoped>
.side-nav {
  width: 260px;
  background: #f1f3ef;
  border-right: 1px solid #e1e6dc;
  padding: 26px 14px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.brand-wrap {
  padding: 6px 12px 24px;
}

.brand {
  font-weight: 700;
  font-size: 22px;
  letter-spacing: 1px;
  color: #4d6542;
  line-height: 1;
}

.slogan {
  margin-top: 10px;
  color: #6f766b;
  font-size: 13px;
  font-weight: 600;
}

.nav-item {
  position: relative;
  border: none;
  background: transparent;
  text-align: left;
  border-radius: 999px;
  padding: 10px 12px;
  font-size: 14px;
  color: #7a7672;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.2s ease;
}

.nav-item:hover {
  background: #e8ece3;
}

.icon-wrap {
  width: 26px;
  height: 26px;
  display: grid;
  place-items: center;
}

.icon {
  width: 22px;
  height: 22px;
}

.nav-item.active {
  background: #e7ebe3;
  color: #546948;
  font-weight: 600;
}

.nav-item.active::before {
  content: '';
  position: absolute;
  left: -14px;
  top: 8px;
  bottom: 8px;
  width: 8px;
  background: #4b653d;
  border-radius: 4px;
  box-shadow: 0 0 0 1px rgba(62, 87, 48, 0.2), 0 0 12px rgba(75, 101, 61, 0.4);
}

@media (max-width: 1400px) {
  .brand {
    font-size: 20px;
  }

  .slogan {
    font-size: 12px;
  }

  .nav-item {
    font-size: 13px;
    padding: 9px 11px;
  }

  .icon-wrap {
    width: 24px;
    height: 24px;
  }

  .icon {
    width: 20px;
    height: 20px;
  }
}
</style>
