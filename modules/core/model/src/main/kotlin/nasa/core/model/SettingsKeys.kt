package nasa.core.model

import alakazam.kotlin.core.PrefPair

object SettingsKeys {
  val AppTheme = PrefPair(key = "appTheme", default = ThemeType.System)
  val ApiKey = PrefPair<ApiKey?>(key = "apiKey", default = null)
}
