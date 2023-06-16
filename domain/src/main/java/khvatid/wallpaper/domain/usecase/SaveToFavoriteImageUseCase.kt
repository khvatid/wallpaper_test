package khvatid.wallpaper.domain.usecase

import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.repository.LocalImagesRepository
class SaveToFavoriteImageUseCase(private val repository: LocalImagesRepository) {

    suspend fun execute(imageModel: ImageModel, isFavorite: Boolean, result: (Throwable?)->Unit){
        if (isFavorite) {
            result(repository.saveImage(imageModel))
        } else {
            result(repository.deleteImage(imageModel.id))
        }
    }

}

