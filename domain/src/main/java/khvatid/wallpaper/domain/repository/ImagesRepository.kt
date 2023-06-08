package khvatid.wallpaper.domain.repository

import khvatid.wallpaper.domain.models.CategoryModel
import khvatid.wallpaper.domain.models.ImageModel
import khvatid.wallpaper.domain.models.Resource
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    suspend fun getCategories(page: Int): Flow<Resource<List<CategoryModel>>>
    suspend fun getImages(category: String, page: Int): Flow<Resource<List<ImageModel>>>
}