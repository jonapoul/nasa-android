package nasa.android.app

import alakazam.android.prefs.core.SimpleStringSerializer
import alakazam.android.prefs.core.getObject
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import nasa.core.model.SettingsKeys
import nasa.core.model.ThemeType
import javax.inject.Inject

internal class ThemePreferences @Inject internal constructor(prefs: FlowSharedPreferences) {
  val theme: Preference<ThemeType> = prefs.getObject(SettingsKeys.AppTheme, ThemeSerializer)

  private object ThemeSerializer : SimpleStringSerializer<ThemeType>(ThemeType::valueOf)
}
