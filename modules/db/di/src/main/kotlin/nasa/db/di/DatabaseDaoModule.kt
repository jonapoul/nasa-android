package nasa.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.RoomNasaDatabase
import nasa.db.apod.ApodDao
import nasa.db.gallery.AlbumDao
import nasa.db.gallery.CenterDao
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.KeywordDao
import nasa.db.gallery.PhotographerDao
import nasa.db.gallery.UrlDao

@Module
@InstallIn(SingletonComponent::class)
class DatabaseDaoModule {
  @Provides
  fun apodDao(db: RoomNasaDatabase): ApodDao = db.apodDao()

  @Provides
  fun galleryDao(db: RoomNasaDatabase): GalleryDao = db.galleryDao()

  @Provides
  fun center(db: RoomNasaDatabase): CenterDao = db.centreDao()

  @Provides
  fun keyword(db: RoomNasaDatabase): KeywordDao = db.keywordDao()

  @Provides
  fun photographer(db: RoomNasaDatabase): PhotographerDao = db.photographerDao()

  @Provides
  fun album(db: RoomNasaDatabase): AlbumDao = db.albumDao()

  @Provides
  fun url(db: RoomNasaDatabase): UrlDao = db.urlDao()
}
