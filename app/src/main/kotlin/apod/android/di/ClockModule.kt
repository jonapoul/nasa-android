package apod.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock

@Module
@InstallIn(SingletonComponent::class)
internal class ClockModule {
  @Provides
  fun clock(): Clock = Clock.System
}
