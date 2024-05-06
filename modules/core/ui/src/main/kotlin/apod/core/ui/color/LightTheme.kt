package apod.core.ui.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal class LightTheme : Theme {
  override val pageBackground = ColorPalette.navy100
  override val pageText = Color(color = 0xFF272630)
  override val pageTextSubdued = ColorPalette.grey400
  override val pageTextPrimary = ColorPalette.blue600
  override val pageTextLoading = ColorPalette.grey500

  override val cardBackground = ColorPalette.white
  override val cardShadow = ColorPalette.navy700

  override val toolbarBackground = ColorPalette.grey700
  override val toolbarBackgroundSubdued = ColorPalette.grey600
  override val toolbarText = ColorPalette.white
  override val toolbarTextSubdued = ColorPalette.grey200
  override val toolbarButton = ColorPalette.white

  override val menuItemBackground = ColorPalette.navy50
  override val menuItemBackgroundSelected = ColorPalette.navy100
  override val menuItemText = ColorPalette.navy900
  override val menuItemTextSelected = ColorPalette.navy900
  override val menuBorder = ColorPalette.navy100

  override val dialogBackground = ColorPalette.white
  override val dialogProgressWheelTrack = ColorPalette.grey100

  override val buttonPrimaryText = Color.White
  override val buttonPrimaryTextSelected = buttonPrimaryText
  override val buttonPrimaryBackground = ColorPalette.blue500
  override val buttonPrimaryBackgroundSelected = ColorPalette.blue300
  override val buttonPrimaryBorder = ColorPalette.blue500
  override val buttonPrimaryShadow = Color.Black.copy(alpha = 0.3f)
  override val buttonPrimaryDisabledText = ColorPalette.grey300
  override val buttonPrimaryDisabledBackground = ColorPalette.grey500

  override val buttonRegularText = ColorPalette.black
  override val buttonRegularTextSelected = ColorPalette.navy900
  override val buttonRegularBackground = ColorPalette.navy200
  override val buttonRegularBackgroundSelected = ColorPalette.grey600
  override val buttonRegularBorder = ColorPalette.navy150
  override val buttonRegularShadow = ColorPalette.black.copy(alpha = 0.2f)
  override val buttonRegularSelectedText = ColorPalette.white
  override val buttonRegularSelectedBackground = ColorPalette.blue600
  override val buttonRegularDisabledText = ColorPalette.navy300
  override val buttonRegularDisabledBackground = ColorPalette.white
  override val buttonRegularDisabledBorder = ColorPalette.navy150

  override val buttonBareText = ColorPalette.navy900
  override val buttonBareTextSelected = ColorPalette.navy900
  override val buttonBareBackground = Color.Transparent
  override val buttonBareBackgroundSelected = Color(color = 0x26646464)
  override val buttonBareBackgroundActive = Color(color = 0x40646464)
  override val buttonBareDisabledText = ColorPalette.navy300
  override val buttonBareDisabledBackground = buttonBareBackground

  override val calendarText = ColorPalette.navy50
  override val calendarBackground = ColorPalette.navy900
  override val calendarItemText = ColorPalette.navy150
  override val calendarItemBackground = ColorPalette.navy800
  override val calendarSelectedBackground = ColorPalette.navy500

  override val successText: Color = ColorPalette.green900
  override val warningBackground = ColorPalette.orange200
  override val warningText = ColorPalette.orange700
  override val warningBorder = ColorPalette.orange500
  override val errorBackground = ColorPalette.red200
  override val errorText = ColorPalette.red500
  override val errorBorder = ColorPalette.red500

  override val formInputBackground = ColorPalette.navy100
  override val formInputShadow = ColorPalette.blue300
  override val formInputText = ColorPalette.navy900
  override val formInputTextPlaceholder = ColorPalette.navy300

  override val checkboxText = ColorPalette.white
  override val checkboxBackgroundSelected = ColorPalette.blue500
  override val checkboxBorderSelected = ColorPalette.blue500
  override val checkboxShadowSelected = ColorPalette.blue300
  override val checkboxToggleBackground = ColorPalette.grey400

  override val preferenceBackground = Color.Transparent
  override val preferenceBackgroundDisabled = ColorPalette.black.copy(alpha = 0.15f)
  override val preferenceForeground = pageText
  override val preferenceForegroundDisabled = ColorPalette.grey400
  override val preferenceSubtitle = ColorPalette.grey400
  override val preferenceSubtitleDisabled = ColorPalette.grey300

  override val progressBarBackground = ColorPalette.grey200
  override val progressBarForeground = pageTextPrimary
}
