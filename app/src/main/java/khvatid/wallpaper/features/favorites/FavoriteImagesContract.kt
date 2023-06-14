package khvatid.wallpaper.features.favorites

import khvatid.wallpaper.domain.models.ImageModel

interface FavoriteImagesContract {
    data class State(
        val listImage: List<ImageModel> = emptyList(),
    )
    sealed class Event{
        object OpenScreen:Event()
    }
}