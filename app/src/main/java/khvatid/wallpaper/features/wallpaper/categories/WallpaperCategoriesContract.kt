package khvatid.wallpaper.features.wallpaper.categories

import khvatid.wallpaper.domain.models.CategoryModel

interface WallpaperCategoriesContract {
    data class State(
        val page: Int = 0,
        val categoriesList: List<CategoryModel> = emptyList(),
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false
    )

    sealed class Event {
        object ScrollToEnd : Event()
    }
}