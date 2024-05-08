package nasa.gallery.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.gallery.data.db.GalleryDao
import nasa.gallery.data.db.GalleryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class GalleryDatabaseModule {
  @Provides
  @Singleton
  fun db(context: Context): GalleryDatabase {
    return Room.databaseBuilder(context, GalleryDatabase::class.java, name = "gallery.db")
      .fallbackToDestructiveMigration()
      .addMigrations(
        // TBC
      )
      .build()
  }

  @Provides
  @Singleton
  fun dao(db: GalleryDatabase): GalleryDao = db.galleryDao()
}
