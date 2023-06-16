package khvatid.wallpaper.data.source

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import khvatid.wallpaper.data.store.datastore.AppDataStore
import khvatid.wallpaper.data.store.datastore.source.AppSettingPreferencesSource
import khvatid.wallpaper.data.store.datastore.source.ThemeSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException



class AppSettingPreferencesSourceImp(private val dataStore: AppDataStore) :
    AppSettingPreferencesSource {
    override suspend fun setThemeSetting(value: ThemeSetting) {
        dataStore.invoke.edit {
            it[isDynamicThemePrefKey] = value.isDynamic
            it[isDarkThemePrefKey] = value.isDark
            it[isSystemThemePrefKey] = value.isSystem
        }
    }

    override fun getThemeSetting(): Flow<ThemeSetting> = dataStore.invoke.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        ThemeSetting(
            isDark = it[isDarkThemePrefKey] ?: false,
            isDynamic = it[isDynamicThemePrefKey] ?: false,
            isSystem = it[isSystemThemePrefKey] ?: true
        )
    }

    private val isDynamicThemePrefKey = booleanPreferencesKey("is_dynamic_theme")
    private val isDarkThemePrefKey = booleanPreferencesKey("is_dark_theme")
    private val isSystemThemePrefKey = booleanPreferencesKey("is_system_theme")
}