package nasa.android.di

import alakazam.kotlin.core.DefaultDispatcher
import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.MainDispatcher
import alakazam.kotlin.core.UnconfinedDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@Suppress("InjectDispatcher")
@InstallIn(SingletonComponent::class)
internal class DispatchersModule {
  @Provides
  @Singleton
  fun ioDispatcher() = IODispatcher(Dispatchers.IO)

  @Provides
  @Singleton
  fun defaultDispatcher() = DefaultDispatcher(Dispatchers.Default)

  @Provides
  @Singleton
  fun mainDispatcher() = MainDispatcher(Dispatchers.Main)

  @Provides
  @Singleton
  fun unconfinedDispatcher() = UnconfinedDispatcher(Dispatchers.Unconfined)
}
