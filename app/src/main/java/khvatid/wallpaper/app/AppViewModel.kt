package khvatid.wallpaper.app


import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.domain.usecase.GetThemeSettingUseCase
import khvatid.wallpaper.features.wallpaper.WallpaperGraphRoutes
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    navHostController: NavHostController,
    getThemeSettingUseCase: GetThemeSettingUseCase
) : ViewModelMVI<AppContract.State, AppContract.Event>() {


    override val state: MutableStateFlow<AppContract.State> =
        MutableStateFlow(AppContract.State(navController = navHostController))

    init {
        viewModelScope.launch {
            getThemeSettingUseCase.execute().collect { theme ->
                state.update { it.copy(themeSettingModel = theme) }
            }
        }
    }

    override fun obtainEvent(event: AppContract.Event) {
        when (event) {
            is AppContract.Event.NavigateToImage -> {
                reduce(event)
            }

            is AppContract.Event.NavigateToSlug -> {
                reduce(event)
            }

            is AppContract.Event.NavigateToFavorite -> {
                reduce(event)
            }

            is AppContract.Event.NavigateToSettings -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: AppContract.Event.NavigateToSettings) {
        if (state.value.route != "settings")
            state.value.navController.navigate(route = "settings")
    }

    private fun reduce(event: AppContract.Event.NavigateToFavorite) {
        if (state.value.route != "favorite")
            state.value.navController.navigate(route = "favorite")

    }

    private fun reduce(event: AppContract.Event.NavigateToImage) {
        state.value.navController.navigate(route = WallpaperGraphRoutes.SingleImage(event.id).route)
    }

    private fun reduce(event: AppContract.Event.NavigateToSlug) {
        state.value.navController.navigate(route = WallpaperGraphRoutes.CategoryImages(event.slug).route)


    }

}