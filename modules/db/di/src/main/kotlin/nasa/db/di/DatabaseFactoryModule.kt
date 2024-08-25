package nasa.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.DefaultApodEntityFactory
import nasa.db.DefaultGalleryEntityFactory
import nasa.db.apod.ApodEntity
import nasa.db.gallery.GalleryEntity

@Module
@InstallIn(SingletonComponent::class)
class DatabaseFactoryModule {
  @Provides
  fun apod(): ApodEntity.Factory = DefaultApodEntityFactory

  @Provides
  fun gallery(): GalleryEntity.Factory = DefaultGalleryEntityFactory
}
