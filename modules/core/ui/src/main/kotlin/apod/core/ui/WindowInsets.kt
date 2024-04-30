package apod.core.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import apod.core.model.ThemeType
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme

@Composable
internal fun SetStatusBarColors(
  type: ThemeType,
  theme: Theme = LocalTheme.current,
  statusBarColor: Color = theme.toolbarBackground,
  navigationBarColor: Color = theme.pageBackground,
  darkTheme: Boolean = isSystemInDarkTheme(),
) {
  val view = LocalView.current

  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.navigationBarColor = navigationBarColor.toArgb()
      window.statusBarColor = statusBarColor.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = when (type) {
        ThemeType.Dark -> false
        ThemeType.Midnight -> false
        ThemeType.Light -> true
        ThemeType.System -> !darkTheme
      }
    }
  }
}
