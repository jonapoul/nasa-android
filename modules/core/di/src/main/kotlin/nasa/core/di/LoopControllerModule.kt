package nasa.core.di

import alakazam.kotlin.core.InfiniteLoopController
import alakazam.kotlin.core.LoopController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Lets us control the flow of infinite loops in unit tests
@Module
@InstallIn(SingletonComponent::class)
class LoopControllerModule {
  @Provides
  @Singleton
  fun loopController(): LoopController = InfiniteLoopController
}
