package khvatid.wallpaper.data.store.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class ImageEntity(

    @PrimaryKey(autoGenerate = false)
    val id : String = "",
    val small: String = "",
    val full: String = "",
    val raw: String = ""
)