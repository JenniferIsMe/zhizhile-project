<script setup lang="ts">
import WorkbenchControlCard from '../components/settings/WorkbenchControlCard.vue'
import FocusWindowSettingsCard from '../components/settings/FocusWindowSettingsCard.vue'
import SettingsPreviewCard from '../components/settings/SettingsPreviewCard.vue'
import { patchSettings, settingsState } from '../stores/settings'
import { pushToast } from '../stores/toast'

/**
 * 手动触发“立即同步”按钮。
 * 当前前端仅做状态通知，真正同步在工作台页由业务逻辑执行。
 */
function syncNow(): void {
  pushToast('info', '已触发同步请求（请在工作台查看同步状态）')
}
</script>

<template>
  <section class="settings-view">
    <header class="header">
      <h2>设置</h2>
      <p>个性化你的编织与阅读环境。</p>
    </header>

    <div class="grid">
      <WorkbenchControlCard
        :auto-wake="settingsState.autoWake"
        :enable-sound="settingsState.enableSound"
        @update-auto-wake="patchSettings({ autoWake: $event })"
        @update-sound="patchSettings({ enableSound: $event })"
        @sync-now="syncNow"
      />

      <FocusWindowSettingsCard
        :mask-step="settingsState.maskStep"
        :mask-opacity="settingsState.maskOpacity"
        @update-mask-step="patchSettings({ maskStep: $event })"
        @update-mask-opacity="patchSettings({ maskOpacity: $event })"
      />

      <SettingsPreviewCard :mask-opacity="settingsState.maskOpacity" />
    </div>
  </section>
</template>

<style scoped>
.settings-view {
  display: grid;
  gap: 12px;
}

.header h2 {
  margin: 0;
}

.header p {
  margin: 6px 0 0;
  color: #60705b;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

@media (max-width: 1200px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
