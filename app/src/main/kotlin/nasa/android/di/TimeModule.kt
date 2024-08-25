package nasa.android.di

import alakazam.kotlin.time.TimeZoneProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.todayIn
import nasa.core.model.Calendar

@Module
@InstallIn(SingletonComponent::class)
class TimeModule {
  @Provides
  fun clock(): Clock = Clock.System

  @Provides
  fun timeZone(): TimeZoneProvider = TimeZoneProvider.Default

  @Provides
  fun calendar(clock: Clock, tzProvider: TimeZoneProvider): Calendar = Calendar {
    clock.todayIn(tzProvider.get())
  }
}
