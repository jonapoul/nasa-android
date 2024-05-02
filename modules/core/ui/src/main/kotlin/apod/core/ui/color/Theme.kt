@file:Suppress("LongParameterList")

package apod.core.ui.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalTheme = compositionLocalOf<Theme> { error("CompositionLocal LocalTheme not present") }

@Immutable
sealed interface Theme {
  val pageBackground: Color
  val pageText: Color
  val pageTextSubdued: Color
  val pageTextPositive: Color
  val pageTextLoading: Color
  val pageTextPositiveLoading: Color

  val cardBackground: Color
  val cardShadow: Color

  val toolbarBackground: Color
  val toolbarText: Color
  val toolbarButton: Color

  val menuItemBackground: Color
  val menuItemBackgroundSelected: Color
  val menuItemText: Color
  val menuItemTextSelected: Color
  val menuBorder: Color

  val dialogBackground: Color
  val dialogProgressWheelTrack: Color

  val buttonPrimaryText: Color
  val buttonPrimaryTextSelected: Color
  val buttonPrimaryBackground: Color
  val buttonPrimaryBackgroundSelected: Color
  val buttonPrimaryBorder: Color
  val buttonPrimaryShadow: Color
  val buttonPrimaryDisabledText: Color
  val buttonPrimaryDisabledBackground: Color
  val buttonPrimaryDisabledBorder: Color

  val buttonRegularText: Color
  val buttonRegularTextSelected: Color
  val buttonRegularBackground: Color
  val buttonRegularBackgroundSelected: Color
  val buttonRegularBorder: Color
  val buttonRegularShadow: Color
  val buttonRegularSelectedText: Color
  val buttonRegularSelectedBackground: Color
  val buttonRegularDisabledText: Color
  val buttonRegularDisabledBackground: Color
  val buttonRegularDisabledBorder: Color

  val buttonBareText: Color
  val buttonBareTextSelected: Color
  val buttonBareBackground: Color
  val buttonBareBackgroundSelected: Color
  val buttonBareBackgroundActive: Color
  val buttonBareDisabledText: Color
  val buttonBareDisabledBackground: Color

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
  val formInputBackgroundSelected: Color
  val formInputBorderSelected: Color
  val formInputShadowSelected: Color
  val formInputText: Color
  val formInputTextPlaceholder: Color
  val formInputTextPlaceholderSelected: Color
  val formInputTextSelected: Color

  val checkboxText: Color
  val checkboxBackgroundSelected: Color
  val checkboxBorderSelected: Color
  val checkboxShadowSelected: Color
  val checkboxToggleBackground: Color

  val preferenceBackground: Color
  val preferenceBackgroundDisabled: Color
  val preferenceForeground: Color
  val preferenceForegroundDisabled: Color
  val preferenceSubtitle: Color
  val preferenceSubtitleDisabled: Color
}
