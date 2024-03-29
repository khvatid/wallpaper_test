package khvatid.wallpaper.data.store.retrofit.source

import khvatid.wallpaper.data.store.retrofit.models.PhotoModel
import khvatid.wallpaper.data.store.ResultModel
import khvatid.wallpaper.data.store.retrofit.models.TopicModel


interface UnsplashApiSource {
    suspend fun getTopics(page: Int): ResultModel<List<TopicModel>>
    suspend fun getPhotos(slug: String, page: Int): ResultModel<List<PhotoModel>>
    suspend fun getPhoto(id: String): ResultModel<PhotoModel>

}