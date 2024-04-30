package apod.settings.keys

import alakazam.android.prefs.core.PrefPair
import apod.core.model.ThemeType

object SettingsKeys {
  val AppTheme = PrefPair(key = "appTheme", default = ThemeType.System)
}
