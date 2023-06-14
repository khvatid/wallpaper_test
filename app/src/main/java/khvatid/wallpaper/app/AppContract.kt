package khvatid.wallpaper.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

interface AppContract {

    @Stable
    data class State(
        val isDynamicTheme: Boolean = false,
        val isShowTopBar: Boolean = true,
        val navController: NavHostController,
    ) {
        val currentRoute: String? @Composable get() =
            navController.currentBackStackEntryAsState().value?.destination?.route


    }

    sealed class Event {
        data class NavigateToSlug(val slug: String) : Event()
        data class NavigateToImage(val id: String) : Event()
        object NavigateToFavorite : Event()
        object NavigateToSettings : Event()
    }

}
