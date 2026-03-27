<script setup lang="ts">
import { computed } from 'vue'
import SideNav from './components/common/SideNav.vue'
import TopBar from './components/common/TopBar.vue'
import GlobalToast from './components/common/GlobalToast.vue'
import LibraryView from './views/LibraryView.vue'
import WorkbenchView from './views/WorkbenchView.vue'
import SettingsView from './views/SettingsView.vue'
import { useRouterState } from './router'

/**
 * 路由状态。
 */
const router = useRouterState()

/**
 * 根据当前路由名切换页面组件。
 */
const activeView = computed(() => {
  if (router.routeName.value === 'workbench') {
    return WorkbenchView
  }

  if (router.routeName.value === 'settings') {
    return SettingsView
  }

  return LibraryView
})
</script>

<template>
  <div class="app-shell">
    <SideNav />
    <div class="main">
      <TopBar />
      <section class="content">
        <component :is="activeView" />
      </section>
    </div>
    <GlobalToast />
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: flex;
  background: #f5f7f3;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.content {
  flex: 1;
  padding: 16px;
  overflow: auto;
}
</style>
