package nasa.settings.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import nasa.core.ui.color.Theme

@Immutable
internal data class PreferenceColors(
  val foreground: Color,
  val background: Color,
  val subtitle: Color,
)

@Stable
internal fun Theme.preference(enabled: Boolean) = PreferenceColors(
  foreground = if (enabled) preferenceForeground else preferenceForegroundDisabled,
  background = if (enabled) preferenceBackground else preferenceBackgroundDisabled,
  subtitle = if (enabled) preferenceSubtitle else preferenceSubtitleDisabled,
)
