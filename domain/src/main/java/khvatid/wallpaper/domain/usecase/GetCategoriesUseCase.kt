package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.CategoryModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformWhile

class GetCategoriesUseCase(private val imagesRepository: ImagesRepository) {
    suspend fun execute(page: Int): Flow<Resource<List<CategoryModel>>> {
        return imagesRepository.getCategories(page).transformWhile {
            emit(it)
            when (it) {
                is Resource.Error -> false
                is Resource.Loading -> true
                is Resource.Success -> false
            }
        }
    }
}