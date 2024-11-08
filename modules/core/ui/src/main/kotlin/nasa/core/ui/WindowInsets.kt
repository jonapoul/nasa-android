package nasa.core.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme

/**
 * [android.view.Window.setNavigationBarColor] and [android.view.Window.setStatusBarColor] are deprecated, and have no
 * effect in Android 15
 */
@Composable
@Suppress("DEPRECATION")
internal fun SetStatusBarColors(
  theme: Theme = LocalTheme.current,
  statusBarColor: Color = theme.toolbarBackground,
  navigationBarColor: Color = theme.pageBackground,
) {
  val view = LocalView.current

  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.navigationBarColor = navigationBarColor.toArgb()
      window.statusBarColor = statusBarColor.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
  }
}
