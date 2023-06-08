package khvatid.wallpaper.features.wallpaper.single

import khvatid.wallpaper.domain.models.ImageModel

interface WallpaperSingleContract {
    data class State(
        val id: String = "",
        val isLoading: Boolean = false,
        val image: ImageModel = ImageModel("","")
    )

    sealed class Event {
        data class OpenScreen(val id: String) : Event()
    }
}