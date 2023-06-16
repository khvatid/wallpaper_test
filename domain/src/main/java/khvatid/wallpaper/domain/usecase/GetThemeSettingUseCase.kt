package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ThemeSettingModel
import khvatid.wallpaper.domain.repository.AppSettingRepository
import kotlinx.coroutines.flow.Flow

class GetThemeSettingUseCase(private val settingRepository: AppSettingRepository) {

    fun execute(): Flow<ThemeSettingModel> = settingRepository.getThemeSetting()

}