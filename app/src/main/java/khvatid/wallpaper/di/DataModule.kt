package khvatid.wallpaper.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import khvatid.wallpaper.data.repository.ImagesRepositoryImp
import khvatid.wallpaper.data.source.UnsplashApiSourceImp
import khvatid.wallpaper.data.store.datastore.AppDataStore
import khvatid.wallpaper.data.store.retrofit.NetworkInstance
import khvatid.wallpaper.data.store.retrofit.source.UnsplashApiSource
import khvatid.wallpaper.domain.repository.ImagesRepository

@Module
@InstallIn(SingletonComponent::class)
class DataModule {


   @Provides
   fun provideAppDataStore(@ApplicationContext context: Context): AppDataStore {
      return AppDataStore(context)
   }

   @Provides
   fun provideNetworkInstance(): NetworkInstance {
      return NetworkInstance()
   }


   @Provides
   fun provideUnsplashApiSource(networkInstance: NetworkInstance):UnsplashApiSource{
      return UnsplashApiSourceImp(networkInstance = networkInstance)
   }

   @Provides
   fun provideImageRepository(unsplashApiSource: UnsplashApiSource):ImagesRepository{
      return ImagesRepositoryImp(unsplashApiSource = unsplashApiSource)
   }

}