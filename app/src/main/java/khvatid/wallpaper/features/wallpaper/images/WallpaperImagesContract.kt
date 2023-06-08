package khvatid.wallpaper.features.wallpaper.images

import khvatid.wallpaper.domain.models.ImageModel

interface WallpaperImagesContract {
    data class State(
        val category: String="",
        val page : Int= 0,
        val listImage: List<ImageModel> = emptyList(),
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false
    )
    sealed class Event{
        object ScrollToEnd:Event()
        data class OpenScreen(val category: String):Event()
    }
}