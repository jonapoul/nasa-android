package nasa.core.di

import alakazam.android.core.UrlOpener
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UrlOpenerModule {
  @Provides
  fun sharedPrefs(context: Context): UrlOpener = UrlOpener(context)
}
