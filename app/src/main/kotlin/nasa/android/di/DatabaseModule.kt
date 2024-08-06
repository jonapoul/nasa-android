package nasa.android.di

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.DefaultApodEntityFactory
import nasa.db.NasaDatabase
import nasa.db.NasaDatabaseDelegate
import nasa.db.RoomAlbumDaoWrapper
import nasa.db.RoomApodDaoWrapper
import nasa.db.RoomCenterDaoWrapper
import nasa.db.RoomGalleryDaoWrapper
import nasa.db.RoomKeywordDaoWrapper
import nasa.db.RoomNasaDatabase
import nasa.db.RoomPhotographerDaoWrapper
import nasa.db.apod.ApodDao
import nasa.db.apod.ApodEntity
import nasa.db.gallery.AlbumDao
import nasa.db.gallery.CenterDao
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.KeywordDao
import nasa.db.gallery.PhotographerDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
  @Provides
  @Singleton
  fun api(impl: RoomNasaDatabase): NasaDatabase = NasaDatabaseDelegate(impl)

  @Provides
  @Singleton
  fun impl(context: Context, io: IODispatcher): RoomNasaDatabase = RoomNasaDatabase.build(context, io)

  @Provides
  @Singleton
  fun apodDao(db: RoomNasaDatabase): ApodDao = RoomApodDaoWrapper(db.apodDao())

  @Provides
  @Singleton
  fun galleryDao(db: RoomNasaDatabase): GalleryDao = RoomGalleryDaoWrapper(db.galleryDao())

  @Provides
  @Singleton
  fun center(db: RoomNasaDatabase): CenterDao = RoomCenterDaoWrapper(db.centreDao())

  @Provides
  @Singleton
  fun keyword(db: RoomNasaDatabase): KeywordDao = RoomKeywordDaoWrapper(db.keywordDao())

  @Provides
  @Singleton
  fun photographer(db: RoomNasaDatabase): PhotographerDao = RoomPhotographerDaoWrapper(db.photographerDao())

  @Provides
  @Singleton
  fun album(db: RoomNasaDatabase): AlbumDao = RoomAlbumDaoWrapper(db.albumDao())

  @Provides
  @Singleton
  fun apodEntityFactory(): ApodEntity.Factory = DefaultApodEntityFactory
}
