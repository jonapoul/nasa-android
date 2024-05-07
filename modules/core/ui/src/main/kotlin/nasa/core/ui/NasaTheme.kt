package nasa.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import nasa.core.model.ThemeType
import nasa.core.ui.color.DarkTheme
import nasa.core.ui.color.LightTheme
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.MidnightTheme
import nasa.core.ui.font.nasaTypography

@Composable
fun NasaTheme(
  type: ThemeType,
  content: @Composable () -> Unit,
) {
  val systemDarkTheme = isSystemInDarkTheme()

  val theme = when (type) {
    ThemeType.System -> if (systemDarkTheme) DarkTheme() else LightTheme()
    ThemeType.Dark -> DarkTheme()
    ThemeType.Light -> LightTheme()
    ThemeType.Midnight -> MidnightTheme()
  }

  CompositionLocalProvider(
    LocalTheme provides theme,
  ) {
    SetStatusBarColors(theme)

    val materialColorScheme = when (theme) {
      is LightTheme -> lightColorScheme(surface = theme.pageBackground)
      is DarkTheme -> darkColorScheme(surface = theme.pageBackground)
      is MidnightTheme -> darkColorScheme(surface = theme.pageBackground)
    }

    MaterialTheme(
      colorScheme = materialColorScheme,
      typography = nasaTypography(theme),
      content = content,
    )
  }
}
