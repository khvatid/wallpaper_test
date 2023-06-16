package khvatid.wallpaper.data.store.datastore.source

import kotlinx.coroutines.flow.Flow

data class ThemeSetting(
  val isDark: Boolean,
  val isDynamic: Boolean,
  val isSystem: Boolean,
)

interface AppSettingPreferencesSource {
  suspend fun setThemeSetting(value: ThemeSetting)
  fun getThemeSetting(): Flow<ThemeSetting>
}