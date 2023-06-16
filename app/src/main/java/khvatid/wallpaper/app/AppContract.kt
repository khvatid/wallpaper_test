package khvatid.wallpaper.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import khvatid.wallpaper.domain.models.ThemeSettingModel

interface AppContract {

    @Stable
    data class State(
        val themeSettingModel: ThemeSettingModel = ThemeSettingModel(
            isDark = true,
            isDynamic = false,
            isSystem = true
        ),
        val isShowTopBar: Boolean = true,
        val navController: NavHostController,
    ) {
        val currentRoute: String?
            @Composable get() =
                navController.currentBackStackEntryAsState().value?.destination?.route
        val route: String get() = navController.currentBackStackEntry?.destination?.route ?: ""

    }

    sealed class Event {
        data class NavigateToSlug(val slug: String) : Event()
        data class NavigateToImage(val id: String) : Event()
        object NavigateToFavorite : Event()
        object NavigateToSettings : Event()
    }

}
