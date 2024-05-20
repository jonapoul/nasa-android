package nasa.core.ui.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal class DarkTheme : Theme {
  override val pageBackground = ColorPalette.grey700
  override val pageText = ColorPalette.navy150
  override val pageTextSubdued = ColorPalette.grey400
  override val pageTextPrimary = ColorPalette.blue200
  override val pageTextLoading = ColorPalette.grey200

  override val cardBackground = ColorPalette.grey600
  override val cardShadow = ColorPalette.navy700

  override val toolbarBackground = ColorPalette.grey900
  override val toolbarBackgroundSubdued = ColorPalette.grey800
  override val toolbarText = ColorPalette.white
  override val toolbarTextSubdued = ColorPalette.grey200
  override val toolbarButton = ColorPalette.white

  override val menuItemBackground = ColorPalette.navy600
  override val menuItemText = ColorPalette.navy100

  override val dialogBackground = ColorPalette.grey600
  override val dialogProgressWheelTrack = ColorPalette.grey600

  override val buttonPrimaryText = Color.White
  override val buttonPrimaryTextSelected = Color.White
  override val buttonPrimaryBackground = ColorPalette.blue400
  override val buttonPrimaryBackgroundSelected = ColorPalette.blue600
  override val buttonPrimaryDisabledText = ColorPalette.navy500
  override val buttonPrimaryDisabledBackground = ColorPalette.grey600

  override val buttonRegularText = ColorPalette.navy150
  override val buttonRegularTextSelected = ColorPalette.navy150
  override val buttonRegularBackground = ColorPalette.navy700
  override val buttonRegularBackgroundSelected = ColorPalette.navy600
  override val buttonRegularDisabledText = ColorPalette.navy500
  override val buttonRegularDisabledBackground = ColorPalette.navy800

  override val calendarText = ColorPalette.navy50
  override val calendarBackground = ColorPalette.navy900
  override val calendarItemText = ColorPalette.navy150
  override val calendarItemBackground = ColorPalette.navy800
  override val calendarSelectedBackground = ColorPalette.blue600

  override val successText: Color = ColorPalette.green500
  override val warningBackground = ColorPalette.orange800
  override val warningText = ColorPalette.orange300
  override val warningBorder = ColorPalette.orange500
  override val errorBackground = ColorPalette.red800
  override val errorText = ColorPalette.red200
  override val errorBorder = ColorPalette.red500

  override val formInputBackground = ColorPalette.grey600
  override val formInputShadow = ColorPalette.blue200
  override val formInputText = ColorPalette.grey100
  override val formInputTextPlaceholder = ColorPalette.navy500

  override val checkboxText = ColorPalette.navy150
  override val checkboxBackgroundSelected = ColorPalette.blue300
  override val checkboxBorderSelected = ColorPalette.blue300
  override val checkboxToggleBackground = ColorPalette.grey700

  override val preferenceBackground = Color.Transparent
  override val preferenceBackgroundDisabled = ColorPalette.black.copy(alpha = 0.35f)
  override val preferenceForeground = pageText
  override val preferenceForegroundDisabled = ColorPalette.grey300
  override val preferenceSubtitle = ColorPalette.grey400
  override val preferenceSubtitleDisabled = ColorPalette.grey500

  override val progressBarBackground = ColorPalette.grey500
  override val progressBarForeground = pageTextPrimary
}
