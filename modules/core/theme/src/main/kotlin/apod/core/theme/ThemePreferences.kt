package apod.core.theme

import alakazam.android.prefs.core.SimpleStringSerializer
import alakazam.android.prefs.core.getObject
import apod.core.model.SettingsKeys
import apod.core.model.ThemeType
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import javax.inject.Inject

class ThemePreferences @Inject internal constructor(prefs: FlowSharedPreferences) {
  val theme: Preference<ThemeType> = prefs.getObject(SettingsKeys.AppTheme, ThemeSerializer)

  private object ThemeSerializer : SimpleStringSerializer<ThemeType>(ThemeType::valueOf)
}
