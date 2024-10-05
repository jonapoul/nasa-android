package nasa.android.app

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.db.NasaDatabase
import nasa.db.getDatabaseBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
  @Provides
  @Singleton
  fun database(context: Context, io: IODispatcher): NasaDatabase {
    val builder = getDatabaseBuilder(context)
    return NasaDatabase.build(builder, io)
  }
}
