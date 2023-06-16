package khvatid.wallpaper.data.repository

import khvatid.wallpaper.data.store.datastore.source.AppSettingPreferencesSource
import khvatid.wallpaper.data.store.datastore.source.ThemeSetting
import khvatid.wallpaper.domain.models.ThemeSettingModel
import khvatid.wallpaper.domain.repository.AppSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingRepositoryImp(private val settingPreferencesSource: AppSettingPreferencesSource) :
    AppSettingRepository {
    override fun getThemeSetting(): Flow<ThemeSettingModel> =
        settingPreferencesSource.getThemeSetting().map { it.toModel() }

    override suspend fun setThemeSetting(value: ThemeSettingModel) {
        settingPreferencesSource.setThemeSetting(value.toThemeSetting())
    }


    private fun ThemeSettingModel.toThemeSetting(): ThemeSetting {
        return ThemeSetting(
            isDark, isDynamic, isSystem
        )
    }

    private fun ThemeSetting.toModel(): ThemeSettingModel {
        return ThemeSettingModel(
            isDark, isDynamic, isSystem
        )
    }
}