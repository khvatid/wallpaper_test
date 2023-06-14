package khvatid.wallpaper.data.store.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import khvatid.wallpaper.data.store.room.entity.ImageEntity
import khvatid.wallpaper.domain.models.ImageModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Insert
    fun insert(imageEntity: ImageEntity)

    @Query("SELECT * FROM images_table")
    fun getList():Flow<List<ImageEntity>>

    @Query("SELECT * FROM images_table WHERE id = :id")
    fun getOne(id: String): Flow<ImageEntity>

    @Query("DELETE FROM images_table WHERE id = :id")
    fun delete(id : String)
}