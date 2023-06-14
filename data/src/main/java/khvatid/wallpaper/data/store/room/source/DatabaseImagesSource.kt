package khvatid.wallpaper.data.store.room.source

import khvatid.wallpaper.data.store.ResultModel
import khvatid.wallpaper.data.store.room.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

interface DatabaseImagesSource {

    suspend fun getImage(id: String): Flow<ImageEntity>
    suspend fun getListImage(): Flow<List<ImageEntity>>

    suspend fun saveImage(imageEntity: ImageEntity):Throwable?

    suspend fun deleteImage(id: String):Throwable?
}