package khvatid.wallpaper.data.repository

import khvatid.wallpaper.data.store.room.entity.ImageEntity
import khvatid.wallpaper.data.store.room.source.DatabaseImagesSource
import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.LocalImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class LocalImagesRepositoryImp(private val imagesSource: DatabaseImagesSource) :
    LocalImagesRepository {
    override suspend fun getImages(): Flow<Resource<List<ImageModel>>> =
        imagesSource.getListImage().map {
            Resource.Success(it.map { imageEntity -> imageEntity.toImageModel() })
        }.catch {
            Resource.Error("${it.message}")
        }

    override suspend fun getImage(id: String): Flow<Resource<ImageModel>> =
        imagesSource.getImage(id).map {
            Resource.Success(it.toImageModel())
        }.catch {
            Resource.Error("${it.message}")
        }

    override suspend fun saveImage(imageModel: ImageModel): Throwable? = try {
        imagesSource.saveImage(imageModel.toImageEntity())
    } catch (e: Exception) {
        e
    }


    override suspend fun deleteImage(id: String): Throwable? = try {
        imagesSource.deleteImage(id)
    } catch (e: Exception) {
        e
    }


    private fun ImageEntity.toImageModel(): ImageModel = ImageModel(
        id = id,
        urls = ImageModel.Urls(
            small = small,
            full = full,
            raw = raw
        )
    )

    private fun ImageModel.toImageEntity(): ImageEntity = ImageEntity(
        id = id,
        small = urls.small,
        full = urls.full,
        raw = urls.raw
    )


}