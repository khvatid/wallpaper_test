package khvatid.wallpaper.data.store.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkInstance {


    private var unsplashApi: UnsplashApi? = null

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

     fun execute(): UnsplashApi {
         synchronized(this) {
             var instance = this.unsplashApi
             if (instance == null) {
                 instance = retrofit.create(UnsplashApi::class.java)
                 this.unsplashApi = instance
             }
             return this.unsplashApi!!
         }
     }

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"
    }
}