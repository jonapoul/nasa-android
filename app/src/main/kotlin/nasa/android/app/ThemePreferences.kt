package nasa.android.app

import dev.jonpoulton.preferences.core.Preference
import dev.jonpoulton.preferences.core.Preferences
import dev.jonpoulton.preferences.core.SimpleStringSerializer
import nasa.core.model.SettingsKeys
import nasa.core.model.ThemeType
import javax.inject.Inject

internal class ThemePreferences @Inject constructor(preferences: Preferences) {
  private val themeSerializer = SimpleStringSerializer(ThemeType::valueOf)

  val theme: Preference<ThemeType> = preferences.getObject(
    key = SettingsKeys.APP_THEME,
    serializer = themeSerializer,
    default = ThemeType.System,
  )
}
