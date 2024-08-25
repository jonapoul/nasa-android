package nasa.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

// Lets us inject an application context without needing to add the hilt dependency to that module
@Module
@InstallIn(SingletonComponent::class)
class ContextModule {
  @Provides
  fun context(
    @ApplicationContext app: Context,
  ): Context = app
}
