package apod.android.di

import alakazam.kotlin.time.TimeZoneProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock

@Module
@InstallIn(SingletonComponent::class)
internal class TimeModule {
  @Provides
  fun clock(): Clock = Clock.System

  @Provides
  fun timeZone(): TimeZoneProvider = TimeZoneProvider.Default
}
