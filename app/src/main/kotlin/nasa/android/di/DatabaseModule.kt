package nasa.android.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.apod.data.db.ApodDao
import nasa.core.db.NasaDatabase
import nasa.gallery.data.db.GalleryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
  @Provides
  @Singleton
  fun db(context: Context): NasaDatabase {
    return Room.databaseBuilder(context, NasaDatabase::class.java, name = "nasa.db")
      .fallbackToDestructiveMigration()
      .addMigrations(
        // TBC
      )
      .build()
  }

  @Provides
  @Singleton
  fun apodDao(db: NasaDatabase): ApodDao = db.apodDao()

  @Provides
  @Singleton
  fun galleryDao(db: NasaDatabase): GalleryDao = db.galleryDao()
}
