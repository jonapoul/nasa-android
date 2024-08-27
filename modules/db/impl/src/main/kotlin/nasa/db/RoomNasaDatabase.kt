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
import nasa.db.apod.ApodDao
import nasa.db.apod.ApodEntity
import nasa.db.gallery.AlbumDao
import nasa.db.gallery.AlbumEntity
import nasa.db.gallery.CenterDao
import nasa.db.gallery.CenterEntity
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.GalleryEntity
import nasa.db.gallery.KeywordDao
import nasa.db.gallery.KeywordEntity
import nasa.db.gallery.PhotographerDao
import nasa.db.gallery.PhotographerEntity
import nasa.db.gallery.UrlDao
import nasa.db.gallery.UrlEntity

@Database(
  version = RoomNasaDatabase.VERSION,
  exportSchema = true,
  entities = [
    ApodEntity::class,
    AlbumEntity::class,
    CenterEntity::class,
    GalleryEntity::class,
    KeywordEntity::class,
    PhotographerEntity::class,
    UrlEntity::class,
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
  abstract fun apodDao(): ApodDao
  abstract fun centreDao(): CenterDao
  abstract fun galleryDao(): GalleryDao
  abstract fun keywordDao(): KeywordDao
  abstract fun photographerDao(): PhotographerDao
  abstract fun albumDao(): AlbumDao
  abstract fun urlDao(): UrlDao

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
