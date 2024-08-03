package nasa.android.di

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.DefaultApodEntityFactory
import nasa.db.NasaDatabase
import nasa.db.NasaDatabaseDelegate
import nasa.db.RoomApodDaoWrapper
import nasa.db.RoomGalleryDaoWrapper
import nasa.db.RoomNasaDatabase
import nasa.db.apod.ApodDao
import nasa.db.apod.ApodEntity
import nasa.db.gallery.GalleryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
  @Provides
  @Singleton
  fun api(impl: RoomNasaDatabase): NasaDatabase = NasaDatabaseDelegate(impl)

  @Provides
  @Singleton
  fun impl(context: Context, io: IODispatcher): RoomNasaDatabase = Room
    .databaseBuilder(context, RoomNasaDatabase::class.java, "nasa.db")
    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
    .setQueryCoroutineContext(io)
    .build()

  @Provides
  @Singleton
  fun apodDao(db: RoomNasaDatabase): ApodDao = RoomApodDaoWrapper(db.apodDao())

  @Provides
  @Singleton
  fun galleryDao(db: RoomNasaDatabase): GalleryDao = RoomGalleryDaoWrapper(db.galleryDao())

  @Provides
  @Singleton
  fun apodEntityFactory(): ApodEntity.Factory = DefaultApodEntityFactory
}
