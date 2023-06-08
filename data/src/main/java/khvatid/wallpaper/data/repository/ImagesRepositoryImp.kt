package khvatid.wallpaper.data.repository

import android.util.Log
import khvatid.wallpaper.data.store.retrofit.models.PhotoModel
import khvatid.wallpaper.data.store.retrofit.models.ResultModel
import khvatid.wallpaper.data.store.retrofit.models.TopicModel
import khvatid.wallpaper.data.store.retrofit.source.UnsplashApiSource
import khvatid.wallpaper.domain.models.CategoryModel
import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import khvatid.wallpaper.domain.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ImagesRepositoryImp(private val unsplashApiSource: UnsplashApiSource) : ImagesRepository {


    override suspend fun getCategories(page: Int): Flow<Resource<List<CategoryModel>>> =
        callbackFlow {
            trySend(Resource.Loading)
            try {
                val result = unsplashApiSource.getTopics(page = page)
                when (result) {
                    is ResultModel.Success -> {
                        trySend(
                            Resource.Success(result.data?.map { it.toCategory() }
                                ?: throw NullPointerException("empty"))
                        )
                    }

                    is ResultModel.Failure -> {
                        trySend(
                            Resource.Error("${result.code} -> ${result.message}")
                        )
                    }
                }
            } catch (e: Exception) {
                trySend(Resource.Error("${e.message}"))
            }
            close()
        }

    override suspend fun getImages(category: String, page: Int): Flow<Resource<List<ImageModel>>> =
        callbackFlow {
            trySend(Resource.Loading)
            try {
                val result = unsplashApiSource
                    .getPhotos(page = page, slug = category)
                when (result) {
                    is ResultModel.Success -> {
                        trySend(
                            Resource.Success(result.data?.map { it.toImage() }
                                ?: throw NullPointerException("empty"))
                        )
                    }

                    is ResultModel.Failure -> {
                        trySend(
                            Resource.Error("${result.code} -> ${result.message}")
                        )
                    }
                }
            } catch (e: Exception) {
                trySend(Resource.Error("${e.message}"))
            }
            close()
        }

    override suspend fun getImage(id: String): Flow<Resource<ImageModel>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            val result = unsplashApiSource
                .getPhoto(id = id)
            when (result) {
                is ResultModel.Success -> {
                    Log.i("REPO", "${result.data}")
                    trySend(
                        Resource.Success(
                            result.data?.toImage()
                                ?: throw NullPointerException("empty")
                        )
                    )
                }

                is ResultModel.Failure -> {
                    trySend(
                        Resource.Error("${result.code} -> ${result.message}")
                    )
                }
            }
        } catch (e: Exception) {
            trySend(Resource.Error("${e.message}"))
        }
        close()
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
        return ImageModel(url = this.urls.regular, id = this.id)
    }
}