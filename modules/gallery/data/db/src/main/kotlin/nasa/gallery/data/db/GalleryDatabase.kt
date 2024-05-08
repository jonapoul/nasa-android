package nasa.gallery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
  version = 1,
  exportSchema = true,
  entities = [
    GalleryEntity::class,
  ],
)
@TypeConverters(
  LocalDateTypeConverter::class,
)
abstract class GalleryDatabase : RoomDatabase() {
  abstract fun galleryDao(): GalleryDao
}
