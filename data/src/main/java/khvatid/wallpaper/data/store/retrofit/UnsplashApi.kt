package khvatid.wallpaper.data.store.retrofit

import khvatid.wallpaper.data.store.retrofit.models.PhotoModel
import khvatid.wallpaper.data.store.retrofit.models.TopicModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface UnsplashApi {

    @GET("/topics")
    suspend fun getTopics(
        @Query("page") page: Int,
        @Query("client_id") clientId: String
    ): Response<List<TopicModel>>

    @GET("/topics/{slug}/photos/")
    suspend fun getTopicPhotos(
        @Path("slug") slug: String,
        @Query("page") page: Int,
        @Query("client_id") clientId: String
    ): Response<List<PhotoModel>>

    @GET("/photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String,
        @Query("client_id") clientId: String
    ): Response<PhotoModel>

}