package nasa.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.RoomNasaDatabase
import nasa.db.apod.ApodDao
import nasa.db.gallery.GalleryDao

@Module
@InstallIn(SingletonComponent::class)
class DatabaseDaoModule {
  @Provides
  fun apodDao(db: RoomNasaDatabase): ApodDao = db.apodDao()

  @Provides
  fun galleryDao(db: RoomNasaDatabase): GalleryDao = db.galleryDao()
}
