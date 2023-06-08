package khvatid.wallpaper.features.wallpaper.images

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.usecase.GetCategoryImagesUseCase
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperImagesViewModel @Inject constructor(
    private val getCategoryImagesUseCase: GetCategoryImagesUseCase
) :
    ViewModelMVI<WallpaperImagesContract.State, WallpaperImagesContract.Event>() {
    override val state: MutableStateFlow<WallpaperImagesContract.State> =
        MutableStateFlow(WallpaperImagesContract.State())

    override fun obtainEvent(event: WallpaperImagesContract.Event) {
        when (event) {
            is WallpaperImagesContract.Event.ScrollToEnd -> {
                reduce(event)
            }

            is WallpaperImagesContract.Event.OpenScreen -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: WallpaperImagesContract.Event.OpenScreen) {
        state.update { it.copy(category = event.category) }
    }

    private fun reduce(event: WallpaperImagesContract.Event.ScrollToEnd) {
        viewModelScope.launch(Dispatchers.IO) {
            val page = state.value.page + 1
            getCategoryImagesUseCase.execute(category = state.value.category, page = page)
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            state.update {
                                it.copy(
                                    isLoading = false,
                                    isEmpty = resource.message == "empty"
                                )
                            }
                        }

                        is Resource.Loading -> {
                            state.update { it.copy(isLoading = true) }
                        }

                        is Resource.Success -> {
                            state.update {
                                it.copy(
                                    isLoading = false,
                                    listImage = it.listImage + resource.data,
                                    page = page,
                                    isEmpty = resource.data.isEmpty()
                                )
                            }
                        }
                    }
                }
        }
    }
}