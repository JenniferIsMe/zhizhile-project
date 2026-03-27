/**
 * 设置持久化键。
 */
const SETTINGS_KEY = 'zzl_settings'

/**
 * 设置模型。
 */
export interface PersistedSettings {
  autoWake: boolean
  enableSound: boolean
  maskStep: number
  maskOpacity: number
}

/**
 * 默认设置值。
 */
const defaultSettings: PersistedSettings = {
  autoWake: false,
  enableSound: false,
  maskStep: 24,
  maskOpacity: 0.4,
}

/**
 * 读取设置。
 */
export function loadSettings(): PersistedSettings {
  const raw = localStorage.getItem(SETTINGS_KEY)
  if (!raw) {
    return defaultSettings
  }

  try {
    const parsed = JSON.parse(raw) as PersistedSettings

    return {
      autoWake: parsed.autoWake ?? defaultSettings.autoWake,
      enableSound: parsed.enableSound ?? defaultSettings.enableSound,
      maskStep: parsed.maskStep ?? defaultSettings.maskStep,
      maskOpacity: parsed.maskOpacity ?? defaultSettings.maskOpacity,
    }
  } catch {
    return defaultSettings
  }
}

/**
 * 保存设置。
 */
export function saveSettings(settings: PersistedSettings): void {
  localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings))
}
