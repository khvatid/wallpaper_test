package khvatid.wallpaper.features.wallpaper.single

import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.flow.MutableStateFlow

class WallpaperSingleViewModel: ViewModelMVI<WallpaperSingleContract.State,WallpaperSingleContract.Event>() {
    override val state: MutableStateFlow<WallpaperSingleContract.State>
        get() = TODO("Not yet implemented")

    override fun obtainEvent(event: WallpaperSingleContract.Event) {
        TODO("Not yet implemented")
    }
}