package khvatid.wallpaper.domain.repository

import khvatid.wallpaper.domain.models.ThemeSettingModel
import kotlinx.coroutines.flow.Flow

interface AppSettingRepository {

    fun getThemeSetting(): Flow<ThemeSettingModel>
    suspend fun setThemeSetting(value : ThemeSettingModel)

}