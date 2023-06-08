package khvatid.wallpaper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import khvatid.wallpaper.domain.repository.ImagesRepository
import khvatid.wallpaper.domain.usecase.GetCategoriesUseCase
import khvatid.wallpaper.domain.usecase.GetCategoryImagesUseCase
import khvatid.wallpaper.domain.usecase.GetImageUseCase


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
}