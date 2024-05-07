package nasa.android.di

import alakazam.android.core.Toaster
import alakazam.kotlin.core.MainDispatcher
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ToasterModule {
  @Provides
  fun toaster(context: Context, main: MainDispatcher): Toaster {
    return Toaster(main, context, context)
  }
}
