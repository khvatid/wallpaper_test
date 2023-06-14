package khvatid.wallpaper.data.source

import khvatid.wallpaper.data.store.retrofit.NetworkInstance
import khvatid.wallpaper.data.store.retrofit.UnsplashApi
import khvatid.wallpaper.data.store.retrofit.models.PhotoModel
import khvatid.wallpaper.data.store.ResultModel
import khvatid.wallpaper.data.store.retrofit.models.TopicModel
import khvatid.wallpaper.data.store.retrofit.source.UnsplashApiSource
import khvatid.wallpaper.data.utils.UNSPLASH_TOKEN
import retrofit2.Response

class UnsplashApiSourceImp(networkInstance: NetworkInstance) : UnsplashApiSource {

    private val service: UnsplashApi = networkInstance.execute()


    override suspend fun getTopics(page: Int): ResultModel<List<TopicModel>> {
        val response: Response<List<TopicModel>> =
            service.getTopics(page = page, clientId = UNSPLASH_TOKEN)
        return if (response.isSuccessful) {
            ResultModel.Success(response.body())
        } else {
            ResultModel.Failure(response.code(), response.message())
        }
    }

    override suspend fun getPhotos(slug: String, page: Int): ResultModel<List<PhotoModel>> {
        val response: Response<List<PhotoModel>> =
            service.getTopicPhotos(slug = slug, page = page, clientId = UNSPLASH_TOKEN)
        return if (response.isSuccessful) {
            ResultModel.Success(response.body())
        } else {
            ResultModel.Failure(response.code(), response.message())
        }
    }

    override suspend fun getPhoto(id: String): ResultModel<PhotoModel> {
        val response: Response<PhotoModel> =
            service.getPhoto(id = id, clientId = UNSPLASH_TOKEN)
        return if (response.isSuccessful) {
            //Log.i("API","${response.body()}")
            ResultModel.Success(response.body())
        } else {
            ResultModel.Failure(response.code(), response.message())
        }
    }


}