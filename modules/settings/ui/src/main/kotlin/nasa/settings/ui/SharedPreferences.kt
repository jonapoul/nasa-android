package nasa.settings.ui

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.preference.PreferenceManager

@Composable
internal fun rememberSharedPreferences(): SharedPreferences {
  val ctx = LocalContext.current
  return remember(ctx) { PreferenceManager.getDefaultSharedPreferences(ctx) }
}
