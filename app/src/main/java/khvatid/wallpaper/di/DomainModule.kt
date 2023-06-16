package khvatid.wallpaper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import khvatid.wallpaper.domain.repository.AppSettingRepository
import khvatid.wallpaper.domain.repository.ImagesRepository
import khvatid.wallpaper.domain.repository.LocalImagesRepository
import khvatid.wallpaper.domain.usecase.GetCategoriesUseCase
import khvatid.wallpaper.domain.usecase.GetCategoryImagesUseCase
import khvatid.wallpaper.domain.usecase.GetFavoriteImageUseCase
import khvatid.wallpaper.domain.usecase.GetFavoriteImagesUseCase
import khvatid.wallpaper.domain.usecase.GetImageUseCase
import khvatid.wallpaper.domain.usecase.GetThemeSettingUseCase
import khvatid.wallpaper.domain.usecase.SaveToFavoriteImageUseCase
import khvatid.wallpaper.domain.usecase.SetThemeSettingUseCase


@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetCategoriesUseCase(imagesRepository: ImagesRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(imagesRepository = imagesRepository)
    }


    @Provides
    fun provideGetCategoryImagesUseCase(imagesRepository: ImagesRepository): GetCategoryImagesUseCase {
        return GetCategoryImagesUseCase(imagesRepository = imagesRepository)
    }

    @Provides
    fun provideGetImageUseCase(imagesRepository: ImagesRepository): GetImageUseCase {
        return GetImageUseCase(imagesRepository)
    }
    @Provides
    fun provideSaveToFavoriteImageUseCase(localImagesRepository: LocalImagesRepository): SaveToFavoriteImageUseCase{
        return SaveToFavoriteImageUseCase(repository = localImagesRepository)
    }

    @Provides
    fun provideGetFavoriteImagesUseCase(localImagesRepository: LocalImagesRepository): GetFavoriteImagesUseCase{
        return GetFavoriteImagesUseCase(repository = localImagesRepository)
    }

    @Provides
    fun provideGetFavoriteImageUseCase(localImagesRepository: LocalImagesRepository): GetFavoriteImageUseCase {
        return GetFavoriteImageUseCase(repository = localImagesRepository)
    }

    @Provides
    fun provideGetThemeSettingUseCase(repository: AppSettingRepository): GetThemeSettingUseCase {
        return GetThemeSettingUseCase(repository)
    }

    @Provides
    fun provideSetThemeSettingUseCase(repository: AppSettingRepository): SetThemeSettingUseCase {
        return SetThemeSettingUseCase(repository)
    }


}