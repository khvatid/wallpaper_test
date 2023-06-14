package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.LocalImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.transformWhile

class GetFavoriteImagesUseCase(private val repository: LocalImagesRepository) {

    suspend fun execute(): Flow<Resource<List<ImageModel>>> = repository.getImages()



}