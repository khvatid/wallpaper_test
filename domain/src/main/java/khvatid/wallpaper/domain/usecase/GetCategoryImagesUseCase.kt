package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformWhile

class GetCategoryImagesUseCase(private val imagesRepository: ImagesRepository) {

    suspend fun execute(category: String, page: Int): Flow<Resource<List<ImageModel>>> {
        return imagesRepository.getImages(category = category, page = page).transformWhile {
            emit(it)
            when (it) {
                is Resource.Error -> false
                is Resource.Loading -> true
                is Resource.Success -> false
            }
        }
    }

}