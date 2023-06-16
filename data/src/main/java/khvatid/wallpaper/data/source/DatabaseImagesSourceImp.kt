package khvatid.wallpaper.data.source

import android.util.Log
import khvatid.wallpaper.data.store.room.dao.ImagesDao
import khvatid.wallpaper.data.store.room.entity.ImageEntity
import khvatid.wallpaper.data.store.room.source.DatabaseImagesSource
import kotlinx.coroutines.flow.Flow

class DatabaseImagesSourceImp(private val imagesDao: ImagesDao) : DatabaseImagesSource {
    override suspend fun getImage(id: String): Flow<ImageEntity?> = imagesDao.getOne(id)

    override suspend fun getListImage(): Flow<List<ImageEntity>> = imagesDao.getList()

    override suspend fun saveImage(imageEntity: ImageEntity): Throwable? = try {
        imagesDao.insert(imageEntity)
        null
    } catch (e: Exception) {
        Log.i("IMAGE_SOURCE", e.stackTraceToString())
        throw e
    }


    override suspend fun deleteImage(id: String): Throwable? = try {
        imagesDao.delete(id)
        null
    } catch (e: Exception) {
        Log.i("IMAGE_SOURCE", e.stackTraceToString())
        throw e
    }


}