package apod.android.di

import alakazam.kotlin.time.TimeZoneProvider
import apod.core.model.Calendar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.todayIn

@Module
@InstallIn(SingletonComponent::class)
internal class TimeModule {
  @Provides
  fun clock(): Clock = Clock.System

  @Provides
  fun timeZone(): TimeZoneProvider = TimeZoneProvider.Default

  @Provides
  fun calendar(clock: Clock, tzProvider: TimeZoneProvider): Calendar = Calendar {
    clock.todayIn(tzProvider.get())
  }
}
