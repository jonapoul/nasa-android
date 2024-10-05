package nasa.android.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.ApodDao
import nasa.db.GalleryDao
import nasa.db.NasaDatabase

@Module
@InstallIn(SingletonComponent::class)
class DatabaseDaoModule {
  @Provides
  fun apodDao(db: NasaDatabase): ApodDao = db.apodDao()

  @Provides
  fun galleryDao(db: NasaDatabase): GalleryDao = db.galleryDao()
}
