package apod.android.di

import alakazam.kotlin.core.IODispatcher
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class PreferencesModule {
  @Provides
  @Singleton
  fun sharedPrefs(context: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
  }

  @Provides
  @Singleton
  fun flowPrefs(sharedPreferences: SharedPreferences, io: IODispatcher): FlowSharedPreferences {
    return FlowSharedPreferences(sharedPreferences, io)
  }
}
