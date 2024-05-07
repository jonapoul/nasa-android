package nasa.apod.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.apod.data.db.ApodDao
import nasa.apod.data.db.ApodDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApodDatabaseModule {
  @Provides
  @Singleton
  fun db(context: Context): ApodDatabase {
    return Room.databaseBuilder(context, ApodDatabase::class.java, name = "apod.db")
      .fallbackToDestructiveMigration()
      .addMigrations(
        // TBC
      )
      .build()
  }

  @Provides
  @Singleton
  fun apodDao(db: ApodDatabase): ApodDao = db.apodDao()
}
