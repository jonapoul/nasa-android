package nasa.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlin.coroutines.CoroutineContext

@Database(
  version = NasaDatabase.VERSION,
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
abstract class NasaDatabase : RoomDatabase() {
  abstract fun apodDao(): ApodDao
  abstract fun galleryDao(): GalleryDao

  companion object {
    const val VERSION = 5

    const val FILENAME = "nasa.db"

    fun build(
      builder: Builder<NasaDatabase>,
      coroutineContext: CoroutineContext,
    ): NasaDatabase = builder
      .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
      .setQueryCoroutineContext(coroutineContext)
      .build()
  }
}
