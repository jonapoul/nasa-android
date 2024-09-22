package nasa.db

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nasa.db.apod.ApodDao
import nasa.db.apod.ApodEntity
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.GalleryEntity

@Database(
  version = RoomNasaDatabase.VERSION,
  exportSchema = true,
  entities = [
    ApodEntity::class,
    GalleryEntity::class,
  ],
  autoMigrations = [
    AutoMigration(from = 1, to = 2), // added keyword, photographer and center tables
    AutoMigration(from = 2, to = 3), // added album table
    AutoMigration(from = 3, to = 4, spec = Migrate3to4::class), // rework gallery table
    AutoMigration(from = 4, to = 5, spec = Migrate4to5::class), // remove tables
  ],
)
@TypeConverters(
  LocalDateTypeConverter::class,
  UrlCollectionTypeConverter::class,
)
abstract class RoomNasaDatabase : RoomDatabase() {
  abstract fun apodDao(): ApodDao
  abstract fun galleryDao(): GalleryDao

  companion object {
    const val VERSION = 5

    private const val FILENAME = "nasa.db"

    fun build(context: Context, io: IODispatcher, filename: String = FILENAME) = Room
      .databaseBuilder(context, RoomNasaDatabase::class.java, filename)
      .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
      .setQueryCoroutineContext(io)
      .build()
  }
}
