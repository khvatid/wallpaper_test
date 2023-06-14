package khvatid.wallpaper.features.wallpaper.single

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.usecase.GetImageUseCase
import khvatid.wallpaper.domain.usecase.SaveToFavoriteImageUseCase
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperSingleViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase,
    private val saveToFavoriteImageUseCase: SaveToFavoriteImageUseCase
) : ViewModelMVI<WallpaperSingleContract.State, WallpaperSingleContract.Event>() {
    override val state: MutableStateFlow<WallpaperSingleContract.State> =
        MutableStateFlow(WallpaperSingleContract.State())

    override fun obtainEvent(event: WallpaperSingleContract.Event) {
        when (event) {
            is WallpaperSingleContract.Event.OpenScreen -> {
                reduce(event)
            }

            is WallpaperSingleContract.Event.AddToFavorite -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: WallpaperSingleContract.Event.AddToFavorite) = viewModelScope.launch {
        saveToFavoriteImageUseCase.execute(state.value.image)
    }


    private fun reduce(event: WallpaperSingleContract.Event.OpenScreen) {
        viewModelScope.launch(Dispatchers.IO) {
            getImageUseCase.execute(id = event.id).collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            state.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }

                        is Resource.Loading -> {
                            state.update { it.copy(isLoading = true) }
                        }

                        is Resource.Success -> {
                            state.update {
                                it.copy(
                                    id = event.id,
                                    image = resource.data,
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }
        }
    }


}
