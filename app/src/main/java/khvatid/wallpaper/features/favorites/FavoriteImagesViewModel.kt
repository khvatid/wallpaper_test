package khvatid.wallpaper.features.favorites

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.usecase.GetFavoriteImagesUseCase
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteImagesViewModel @Inject constructor(
    private val getFavoriteImagesUseCase: GetFavoriteImagesUseCase
) :
    ViewModelMVI<FavoriteImagesContract.State, FavoriteImagesContract.Event>() {
    override val state: MutableStateFlow<FavoriteImagesContract.State> =
        MutableStateFlow(FavoriteImagesContract.State())

    override fun obtainEvent(event: FavoriteImagesContract.Event) {
        when (event) {
            is FavoriteImagesContract.Event.OpenScreen -> reduce(event)
        }
    }

    private fun reduce(event: FavoriteImagesContract.Event.OpenScreen) {
        viewModelScope.launch {
            getFavoriteImagesUseCase.execute().collect{
                when (it) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        state.update { value ->
                            value.copy(listImage = it.data)
                        }
                    }
                }
            }
        }
    }
}