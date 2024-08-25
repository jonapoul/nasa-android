package nasa.db.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.RoomAlbumDaoWrapper
import nasa.db.RoomApodDaoWrapper
import nasa.db.RoomCenterDaoWrapper
import nasa.db.RoomGalleryDaoWrapper
import nasa.db.RoomKeywordDaoWrapper
import nasa.db.RoomPhotographerDaoWrapper
import nasa.db.RoomUrlDaoWrapper
import nasa.db.apod.ApodDao
import nasa.db.gallery.AlbumDao
import nasa.db.gallery.CenterDao
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.KeywordDao
import nasa.db.gallery.PhotographerDao
import nasa.db.gallery.UrlDao

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseDaoModule {
  @Binds
  fun apodDao(impl: RoomApodDaoWrapper): ApodDao

  @Binds
  fun galleryDao(impl: RoomGalleryDaoWrapper): GalleryDao

  @Binds
  fun center(impl: RoomCenterDaoWrapper): CenterDao

  @Binds
  fun keyword(impl: RoomKeywordDaoWrapper): KeywordDao

  @Binds
  fun photographer(impl: RoomPhotographerDaoWrapper): PhotographerDao

  @Binds
  fun album(impl: RoomAlbumDaoWrapper): AlbumDao

  @Binds
  fun url(impl: RoomUrlDaoWrapper): UrlDao
}
