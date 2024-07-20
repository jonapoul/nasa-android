package nasa.android.di

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.apod.data.db.ApodDao
import nasa.db.NasaDatabase
import nasa.db.RoomApodDaoWrapper
import nasa.db.RoomGalleryDaoWrapper
import nasa.gallery.data.db.GalleryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
  @Provides
  @Singleton
  fun db(context: Context, io: IODispatcher): NasaDatabase {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("nasa.db")
    return Room
      .databaseBuilder<NasaDatabase>(context, name = dbFile.absolutePath)
      .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
//      .addMigrations()
//      .setDriver(BundledSQLiteDriver())
      .setQueryCoroutineContext(io)
      .build()
  }

  @Provides
  @Singleton
  fun apodDao(db: NasaDatabase): ApodDao = RoomApodDaoWrapper(db.apodDao())

  @Provides
  @Singleton
  fun galleryDao(db: NasaDatabase): GalleryDao = RoomGalleryDaoWrapper(db.galleryDao())
}
