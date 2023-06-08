package khvatid.wallpaper.features.wallpaper.categories

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.usecase.GetCategoriesUseCase
import khvatid.wallpaper.utils.ViewModelMVI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperCategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) :
    ViewModelMVI<WallpaperCategoriesContract.State, WallpaperCategoriesContract.Event>() {
    override val state: MutableStateFlow<WallpaperCategoriesContract.State> =
        MutableStateFlow(WallpaperCategoriesContract.State())

    override fun obtainEvent(event: WallpaperCategoriesContract.Event) {
        when (event) {
            is WallpaperCategoriesContract.Event.ScrollToEnd -> {
                reduce(event)
            }
        }
    }

    private fun reduce(event: WallpaperCategoriesContract.Event.ScrollToEnd) {
        viewModelScope.launch(Dispatchers.IO) {
            val page = state.value.page + 1
            getCategoriesUseCase.execute(page).collect { resource ->
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
                                categoriesList = it.categoriesList + resource.data,
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