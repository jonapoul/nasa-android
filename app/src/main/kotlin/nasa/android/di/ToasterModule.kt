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
class ToasterModule {
  @Provides
  fun toaster(context: Context, main: MainDispatcher): Toaster = Toaster(main, context, context)
}
