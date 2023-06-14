package khvatid.wallpaper.data.store.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import khvatid.wallpaper.data.store.room.dao.ImagesDao
import khvatid.wallpaper.data.store.room.entity.ImageEntity

@Database(
    entities = [(ImageEntity::class)],
    exportSchema = true,
    version = 1,
)
abstract class WallpaperAppDatabase : RoomDatabase(){
    abstract fun imagesDao(): ImagesDao

    companion object {
        private var INSTANCE: WallpaperAppDatabase? = null

        fun getInstance(context: Context): WallpaperAppDatabase {
            synchronized(this) {
                var instance: WallpaperAppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WallpaperAppDatabase::class.java,
                        "wallpaper_app_database"
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}