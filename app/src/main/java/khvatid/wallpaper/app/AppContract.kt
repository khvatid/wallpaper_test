package khvatid.wallpaper.app

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

interface AppContract {

    @Stable
    data class State(
        val isDynamicTheme: Boolean = false,
        val navController: NavHostController,
    )

    sealed class Event {
        data class NavigateToSlug(val slug: String) : Event()
        data class NavigateToImage(val id: String) : Event()
    }

}
