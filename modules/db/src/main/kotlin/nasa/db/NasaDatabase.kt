package nasa.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
  version = 1,
  exportSchema = true,
  entities = [
    RoomApodEntity::class,
    RoomGalleryEntity::class,
  ],
)
@TypeConverters(
  LocalDateTypeConverter::class,
)
abstract class NasaDatabase : RoomDatabase() {
  abstract fun apodDao(): RoomApodDao
  abstract fun galleryDao(): RoomGalleryDao
}
