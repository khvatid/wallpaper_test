package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.LocalImagesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteImageUseCase(private val repository: LocalImagesRepository) {

    suspend fun execute(id: String): Flow<Resource<ImageModel>> = repository.getImage(id)

}