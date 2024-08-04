@file:Suppress("LongParameterList", "CompositionLocalAllowlist")

package nasa.core.ui.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalTheme = compositionLocalOf<Theme> { error("CompositionLocal LocalTheme not present") }

@Immutable
sealed interface Theme {
  val pageBackground: Color
  val pageText: Color
  val pageTextSubdued: Color
  val pageTextPrimary: Color
  val pageTextLoading: Color

  val cardBackground: Color
  val cardShadow: Color

  val toolbarBackground: Color
  val toolbarBackgroundSubdued: Color
  val toolbarText: Color
  val toolbarTextSubdued: Color
  val toolbarButton: Color

  val menuItemBackground: Color
  val menuItemText: Color

  val dialogBackground: Color
  val dialogProgressWheelTrack: Color

  val buttonPrimaryText: Color
  val buttonPrimaryTextSelected: Color
  val buttonPrimaryBackground: Color
  val buttonPrimaryBackgroundSelected: Color
  val buttonPrimaryDisabledText: Color
  val buttonPrimaryDisabledBackground: Color

  val buttonRegularText: Color
  val buttonRegularTextSelected: Color
  val buttonRegularBackground: Color
  val buttonRegularBackgroundSelected: Color
  val buttonRegularDisabledText: Color
  val buttonRegularDisabledBackground: Color

  val calendarText: Color
  val calendarBackground: Color
  val calendarItemText: Color
  val calendarItemBackground: Color
  val calendarSelectedBackground: Color

  val successText: Color
  val warningBackground: Color
  val warningText: Color
  val warningBorder: Color
  val errorBackground: Color
  val errorText: Color
  val errorBorder: Color

  val formInputBackground: Color
  val formInputBackgroundDialog: Color
  val formInputShadow: Color
  val formInputText: Color
  val formInputTextPlaceholder: Color

  val checkboxText: Color
  val checkboxBackgroundSelected: Color
  val checkboxBorderSelected: Color
  val checkboxToggleBackground: Color

  val preferenceBackground: Color
  val preferenceBackgroundDisabled: Color
  val preferenceForeground: Color
  val preferenceForegroundDisabled: Color
  val preferenceSubtitle: Color
  val preferenceSubtitleDisabled: Color

  val progressBarBackground: Color
  val progressBarForeground: Color

  val scrollbar: Color
  val scrollbarSelected: Color
}
