package khvatid.wallpaper.features.wallpaper.single

import android.content.Context
import android.graphics.Bitmap
import khvatid.wallpaper.domain.models.ImageModel

interface WallpaperSingleContract {
    data class State(
        val id: String = "",
        val isLoading: Boolean = true,
        val isFavorite: Boolean = false,
        val image: ImageModel = ImageModel(),
    )

    sealed class Event {
        data class OpenScreen(val id: String) : Event()
        object AddToFavorite : Event()
        data class SetAs(val context: Context, val bitmap: Bitmap) : Event()
        data class ShareImage(val context: Context, val bitmap: Bitmap) : Event()

    }
}