package nasa.db

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec

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
    RoomUrlEntity::class,
  ],
  autoMigrations = [
    AutoMigration(from = 1, to = 2), // added keyword, photographer and center tables
    AutoMigration(from = 2, to = 3), // added album table
    AutoMigration(from = 3, to = 4, spec = RoomNasaDatabase.Migrate3to4::class), // rework gallery table
  ],
)
@TypeConverters(
  LocalDateTypeConverter::class,
  UrlCollectionTypeConverter::class,
)
abstract class RoomNasaDatabase : RoomDatabase() {
  abstract fun apodDao(): RoomApodDao
  abstract fun centreDao(): RoomCenterDao
  abstract fun galleryDao(): RoomGalleryDao
  abstract fun keywordDao(): RoomKeywordDao
  abstract fun photographerDao(): RoomPhotographerDao
  abstract fun albumDao(): RoomAlbumDao
  abstract fun urlDao(): RoomUrlCollectionDao

  @RenameColumn(tableName = "gallery", fromColumnName = "date", toColumnName = "id")
  @DeleteColumn(tableName = "gallery", columnName = "date")
  class Migrate3to4 : AutoMigrationSpec

  companion object {
    const val VERSION = 4

    private const val FILENAME = "nasa.db"

    fun build(context: Context, io: IODispatcher, filename: String = FILENAME) = Room
      .databaseBuilder(context, RoomNasaDatabase::class.java, filename)
      .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
      .setQueryCoroutineContext(io)
      .build()
  }
}
