package khvatid.wallpaper.app


import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.features.wallpaper.WallpaperGraphRoutes
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    navHostController: NavHostController,
) : ViewModelMVI<AppContract.State, AppContract.Event>() {


    override val state: MutableStateFlow<AppContract.State> =
        MutableStateFlow(AppContract.State(navController = navHostController))


    override fun obtainEvent(event: AppContract.Event) {
        when (event) {
            is AppContract.Event.NavigateToImage -> {
                reduce(event)
            }

            is AppContract.Event.NavigateToSlug -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: AppContract.Event.NavigateToImage) {
        state.value.navController.navigate(route = WallpaperGraphRoutes.SingleImage(event.id).route)
    }

    private fun reduce(event: AppContract.Event.NavigateToSlug) {
        state.value.navController.navigate(route = WallpaperGraphRoutes.CategoryImages(event.slug).route)


    }

}