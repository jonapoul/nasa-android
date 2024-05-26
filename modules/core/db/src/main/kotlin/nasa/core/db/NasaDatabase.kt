package nasa.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nasa.apod.data.db.ApodDao
import nasa.apod.data.db.ApodEntity
import nasa.gallery.data.db.GalleryDao
import nasa.gallery.data.db.GalleryEntity

@Database(
  version = 1,
  exportSchema = true,
  entities = [
    ApodEntity::class,
    GalleryEntity::class,
  ],
)
@TypeConverters(
  LocalDateTypeConverter::class,
  ApodMediaTypeConverter::class,
)
abstract class NasaDatabase : RoomDatabase() {
  abstract fun apodDao(): ApodDao
  abstract fun galleryDao(): GalleryDao
}
