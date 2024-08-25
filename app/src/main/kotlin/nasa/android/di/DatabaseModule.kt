package nasa.android.di

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.NasaDatabase
import nasa.db.NasaDatabaseDelegate
import nasa.db.RoomNasaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
  @Provides
  @Singleton
  fun api(impl: RoomNasaDatabase): NasaDatabase = NasaDatabaseDelegate(impl)

  @Provides
  @Singleton
  fun impl(context: Context, io: IODispatcher): RoomNasaDatabase = RoomNasaDatabase.build(context, io)
}
