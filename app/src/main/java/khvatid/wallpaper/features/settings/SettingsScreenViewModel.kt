package khvatid.wallpaper.features.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.androidAi.features.settings.SettingsScreenContract
import khvatid.wallpaper.domain.models.ThemeSettingModel
import khvatid.wallpaper.domain.usecase.GetThemeSettingUseCase
import khvatid.wallpaper.domain.usecase.SetThemeSettingUseCase
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val getThemeSettingUseCase: GetThemeSettingUseCase,
    private val setThemeSettingUseCase: SetThemeSettingUseCase,
) : ViewModelMVI<SettingsScreenContract.State, SettingsScreenContract.Events>() {
    override val state: MutableStateFlow<SettingsScreenContract.State> = MutableStateFlow(
        SettingsScreenContract.State()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getThemeSettingUseCase.execute().collect { theme ->
                state.update {
                    it.copy(
                        isDarkTheme = theme.isDark,
                        isDynamicTheme = theme.isDynamic,
                        isSystemTheme = theme.isSystem
                    )
                }
            }
        }
    }


    override fun obtainEvent(event: SettingsScreenContract.Events) {
        when (event) {
            is SettingsScreenContract.Events.UseDynamicTheme -> {
                reduce(event)
            }

            is SettingsScreenContract.Events.UseDarkTheme -> {
                reduce(event)
            }

            is SettingsScreenContract.Events.UseSystemTheme -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: SettingsScreenContract.Events.UseSystemTheme) {
        viewModelScope.launch {
            setThemeSettingUseCase.execute(
                ThemeSettingModel(
                    isDark = state.value.isDarkTheme,
                    isDynamic = state.value.isDynamicTheme,
                    isSystem = !state.value.isSystemTheme
                )
            )
        }
    }

    private fun reduce(event: SettingsScreenContract.Events.UseDarkTheme) {
        viewModelScope.launch {
            setThemeSettingUseCase.execute(
                ThemeSettingModel(
                    isDark = !state.value.isDarkTheme,
                    isDynamic = state.value.isDynamicTheme,
                    isSystem = state.value.isSystemTheme
                )
            )
        }
    }


    private fun reduce(event: SettingsScreenContract.Events.UseDynamicTheme) {
        viewModelScope.launch {
            setThemeSettingUseCase.execute(
                ThemeSettingModel(
                    isDark = state.value.isDarkTheme,
                    isDynamic = !state.value.isDynamicTheme,
                    isSystem = state.value.isSystemTheme
                )
            )
        }
    }


    override fun onCleared() {
        viewModelScope.cancel()
        Log.i("SETTINGS", "ViewModelScope is clear")
        super.onCleared()
    }

}