package apod.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import apod.core.model.ThemeType
import apod.core.ui.color.DarkTheme
import apod.core.ui.color.LightTheme
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.MidnightTheme
import apod.core.ui.color.Theme
import apod.core.ui.font.apodTypography

@Composable
fun ApodTheme(
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
    SetStatusBarColors(
      type = type,
      theme = theme,
      darkTheme = systemDarkTheme,
    )

    val materialColorScheme = when (theme) {
      is LightTheme -> lightColorScheme(surface = theme.pageBackground)
      is DarkTheme -> darkColorScheme(surface = theme.pageBackground)
      is MidnightTheme -> darkColorScheme(surface = theme.pageBackground)
    }

    MaterialTheme(
      colorScheme = materialColorScheme,
      typography = apodTypography(theme),
      content = content,
    )
  }
}
