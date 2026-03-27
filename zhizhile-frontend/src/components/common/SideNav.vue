<script setup lang="ts">
import { useRouterState, navigate } from '../../router'

/**
 * 侧边栏导航数据。
 * 只保留当前 MVP 范围内的页面入口。
 */
const navItems = [
  { key: 'library', label: '图解库', path: '/library' },
  { key: 'settings', label: '设置', path: '/settings' },
]

const router = useRouterState()

/**
 * 处理导航点击。
 */
function handleClick(path: string): void {
  navigate(path)
}
</script>

<template>
  <aside class="side-nav">
    <div class="brand">织织乐</div>
    <button
      v-for="item in navItems"
      :key="item.key"
      class="nav-item"
      :class="{ active: router.routeName.value === item.key }"
      @click="handleClick(item.path)"
    >
      {{ item.label }}
    </button>
  </aside>
</template>

<style scoped>
.side-nav {
  width: 220px;
  background: #f4f6f2;
  border-right: 1px solid #e4e7df;
  padding: 20px 14px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.brand {
  font-weight: 700;
  font-size: 22px;
  color: #415335;
  margin: 10px 12px 20px;
}

.nav-item {
  border: none;
  background: transparent;
  text-align: left;
  border-radius: 10px;
  padding: 10px 12px;
  font-size: 14px;
  color: #394238;
  cursor: pointer;
}

.nav-item.active {
  background: #dfe8d9;
  color: #21311b;
  font-weight: 600;
}
</style>
