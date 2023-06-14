package khvatid.wallpaper.data.repository

import khvatid.wallpaper.data.store.retrofit.models.PhotoModel
import khvatid.wallpaper.data.store.ResultModel
import khvatid.wallpaper.data.store.retrofit.models.TopicModel
import khvatid.wallpaper.data.store.retrofit.source.UnsplashApiSource
import khvatid.wallpaper.domain.models.CategoryModel
import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.ImagesRepository
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch

class ImagesRepositoryImp(private val unsplashApiSource: UnsplashApiSource) : ImagesRepository {

    private inline fun <T, R> ProducerScope<Resource<R>>.handleResult(
        result: ResultModel<T>,
        map: (T) -> R
    ) = when (result) {
        is ResultModel.Failure -> trySend(Resource.Error("${result.code} -> ${result.message}"))
        is ResultModel.Success -> trySend(
            Resource.Success(
                map(result.data ?: throw NullPointerException())
            )
        )
    }

    override suspend fun getCategories(page: Int): Flow<Resource<List<CategoryModel>>> =
        callbackFlow {
            trySend(Resource.Loading)
            val result = unsplashApiSource.getTopics(page = page)
            handleResult(result) {
                it.map { topicModel -> topicModel.toCategory() }
            }
            close()
        }.catch {
            emit(Resource.Error("${it.message}"))
        }

    override suspend fun getImages(category: String, page: Int): Flow<Resource<List<ImageModel>>> =
        callbackFlow {
            trySend(Resource.Loading)
            val result = unsplashApiSource
                .getPhotos(page = page, slug = category)
            handleResult(result = result) { photoModels ->
                photoModels.map { it.toImage() }
            }
            close()
        }.catch {
            emit(Resource.Error("${it.message}"))
        }

    override suspend fun getImage(id: String): Flow<Resource<ImageModel>> = callbackFlow {
        trySend(Resource.Loading)
        val result = unsplashApiSource.getPhoto(id = id)
        handleResult(result = result) {
            it.toImage()
        }
        close()
    }.catch {
        emit(Resource.Error("${it.message}"))
    }


    private fun TopicModel.toCategory(): CategoryModel {
        return CategoryModel(
            label = this.title,
            description = this.description,
            slug = this.slug,
            imageUrl = this.cover_photo.urls.small
        )
    }

    private fun PhotoModel.toImage(): ImageModel {
        return ImageModel(
            id = this.id,
            urls = ImageModel.Urls(
                small = this.urls.small,
                full = this.urls.full,
                raw = this.urls.raw
            )
        )
    }
}