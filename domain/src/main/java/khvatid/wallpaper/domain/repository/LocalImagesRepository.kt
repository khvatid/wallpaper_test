package khvatid.wallpaper.domain.repository

import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface LocalImagesRepository {

   suspend fun getImages(): Flow<Resource<List<ImageModel>>>
   suspend fun getImage(id: String): Flow<Resource<ImageModel>>
   suspend fun saveImage(imageModel: ImageModel): Throwable?
   suspend fun deleteImage(id : String):Throwable?

}