import { reactive } from 'vue'
import { loadSettings, saveSettings } from '../utils/settingsPersist'

/**
 * 设置状态域。
 * 初始化时先读取本地持久化结果，避免刷新后丢失用户偏好。
 */
const initial = loadSettings()

export const settingsState = reactive({
  autoWake: initial.autoWake,
  enableSound: initial.enableSound,
  maskStep: initial.maskStep,
  maskOpacity: initial.maskOpacity,
})

/**
 * 统一写入设置并同步持久化。
 */
export function patchSettings(payload: Partial<typeof settingsState>): void {
  Object.assign(settingsState, payload)
  saveSettings({
    autoWake: settingsState.autoWake,
    enableSound: settingsState.enableSound,
    maskStep: settingsState.maskStep,
    maskOpacity: settingsState.maskOpacity,
  })
}
