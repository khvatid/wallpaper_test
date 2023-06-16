package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ThemeSettingModel
import khvatid.wallpaper.domain.repository.AppSettingRepository


class SetThemeSettingUseCase(private val settingRepository: AppSettingRepository) {

    suspend fun execute(value: ThemeSettingModel) {
        settingRepository.setThemeSetting(value = value)
    }

}