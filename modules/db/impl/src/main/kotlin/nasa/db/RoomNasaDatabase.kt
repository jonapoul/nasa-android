package nasa.db

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
  version = RoomNasaDatabase.VERSION,
  exportSchema = true,
  entities = [
    RoomApodEntity::class,
    RoomAlbumEntity::class,
    RoomCenterEntity::class,
    RoomGalleryEntity::class,
    RoomKeywordEntity::class,
    RoomPhotographerEntity::class,
  ],
  autoMigrations = [
    AutoMigration(from = 1, to = 2), // added keyword, photographer and center tables
    AutoMigration(from = 2, to = 3), // added album table
  ],
)
@TypeConverters(
  LocalDateTypeConverter::class,
)
abstract class RoomNasaDatabase : RoomDatabase() {
  abstract fun apodDao(): RoomApodDao
  abstract fun centreDao(): RoomCenterDao
  abstract fun galleryDao(): RoomGalleryDao
  abstract fun keywordDao(): RoomKeywordDao
  abstract fun photographerDao(): RoomPhotographerDao
  abstract fun albumDao(): RoomAlbumDao

  companion object {
    const val VERSION = 3

    private const val FILENAME = "nasa.db"

    fun build(context: Context, io: IODispatcher, filename: String = FILENAME) = Room
      .databaseBuilder(context, RoomNasaDatabase::class.java, filename)
      .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
      .setQueryCoroutineContext(io)
      .build()
  }
}
