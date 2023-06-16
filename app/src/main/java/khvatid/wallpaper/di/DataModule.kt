package khvatid.wallpaper.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import khvatid.wallpaper.data.repository.AppSettingRepositoryImp
import khvatid.wallpaper.data.repository.ImagesRepositoryImp
import khvatid.wallpaper.data.repository.LocalImagesRepositoryImp
import khvatid.wallpaper.data.source.AppSettingPreferencesSourceImp
import khvatid.wallpaper.data.source.DatabaseImagesSourceImp
import khvatid.wallpaper.data.source.UnsplashApiSourceImp
import khvatid.wallpaper.data.store.datastore.AppDataStore
import khvatid.wallpaper.data.store.datastore.source.AppSettingPreferencesSource
import khvatid.wallpaper.data.store.retrofit.NetworkInstance
import khvatid.wallpaper.data.store.retrofit.source.UnsplashApiSource
import khvatid.wallpaper.data.store.room.WallpaperAppDatabase
import khvatid.wallpaper.data.store.room.source.DatabaseImagesSource
import khvatid.wallpaper.domain.repository.AppSettingRepository
import khvatid.wallpaper.domain.repository.ImagesRepository
import khvatid.wallpaper.domain.repository.LocalImagesRepository

@Module
@InstallIn(SingletonComponent::class)
class DataModule {


   @Provides
   fun provideAppDataStore(@ApplicationContext context: Context): AppDataStore {
      return AppDataStore(context)

   }

    @Provides
    fun provideAppSettingsPreferencesSource(dataStore: AppDataStore): AppSettingPreferencesSource {
        return AppSettingPreferencesSourceImp(dataStore = dataStore)
    }


    @Provides
    fun provideAppSettingRepository(settingPreferencesSource: AppSettingPreferencesSource): AppSettingRepository {
        return AppSettingRepositoryImp(settingPreferencesSource = settingPreferencesSource)
    }

   @Provides
   fun provideWallpaperAppDatabase(@ApplicationContext context: Context):WallpaperAppDatabase{
      return WallpaperAppDatabase.getInstance(context)
   }

   @Provides
   fun provideDatabaseImagesSource(database: WallpaperAppDatabase):DatabaseImagesSource{
      return DatabaseImagesSourceImp(imagesDao = database.imagesDao())
   }

   @Provides
  fun provideLocalImageRepository(imageSource: DatabaseImagesSource):LocalImagesRepository{
     return LocalImagesRepositoryImp(imagesSource = imageSource)
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