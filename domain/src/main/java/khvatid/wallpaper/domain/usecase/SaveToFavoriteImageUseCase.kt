package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.repository.LocalImagesRepository

class SaveToFavoriteImageUseCase(private val repository: LocalImagesRepository) {

    suspend fun execute(imageModel: ImageModel):Throwable? = repository.saveImage(imageModel)

}