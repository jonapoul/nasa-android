package nasa.core.ui.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal class MidnightTheme : Theme {
  override val pageBackground = ColorPalette.grey900
  override val pageBackgroundAlt = ColorPalette.grey800
  override val pageText = ColorPalette.grey100
  override val pageTextSubdued = ColorPalette.grey400
  override val pageTextPrimary = ColorPalette.blue200
  override val pageTextLoading = ColorPalette.grey300

  override val cardBackground = ColorPalette.grey800
  override val cardShadow = ColorPalette.grey900

  override val toolbarBackground = ColorPalette.grey700
  override val toolbarBackgroundSubdued = ColorPalette.grey800
  override val toolbarText = ColorPalette.white
  override val toolbarTextSubdued = ColorPalette.grey200
  override val toolbarButton = ColorPalette.white

  override val menuItemBackground = ColorPalette.grey600
  override val menuItemText = ColorPalette.grey100

  override val dialogBackground = ColorPalette.grey700
  override val dialogProgressWheelTrack = ColorPalette.grey700

  override val buttonPrimaryText = Color.White
  override val buttonPrimaryTextSelected = Color.Black
  override val buttonPrimaryBackground = ColorPalette.blue800
  override val buttonPrimaryBackgroundSelected = ColorPalette.blue500
  override val buttonPrimaryDisabledText = ColorPalette.grey500
  override val buttonPrimaryDisabledBackground = ColorPalette.grey700

  override val buttonRegularText = ColorPalette.grey150
  override val buttonRegularTextSelected = ColorPalette.grey150
  override val buttonRegularBackground = ColorPalette.grey700
  override val buttonRegularBackgroundSelected = ColorPalette.grey500
  override val buttonRegularDisabledText = ColorPalette.grey500
  override val buttonRegularDisabledBackground = ColorPalette.grey700

  override val calendarText = ColorPalette.grey50
  override val calendarBackground = ColorPalette.grey700
  override val calendarItemText = ColorPalette.grey150
  override val calendarItemBackground = ColorPalette.grey500
  override val calendarSelectedBackground = ColorPalette.blue500

  override val successText: Color = ColorPalette.green400
  override val warningBackground = ColorPalette.orange800
  override val warningText = ColorPalette.orange200
  override val warningBorder = ColorPalette.orange500
  override val errorBackground = ColorPalette.red800
  override val errorText = ColorPalette.red200
  override val errorBorder = ColorPalette.red500

  override val formInputBackground = ColorPalette.grey800
  override val formInputBackgroundDialog = ColorPalette.grey800
  override val formInputShadow = ColorPalette.blue400
  override val formInputText = ColorPalette.grey50
  override val formInputTextPlaceholder = ColorPalette.grey400

  override val checkboxChecked = ColorPalette.blue300
  override val checkboxCheckedDisabled = ColorPalette.grey400
  override val checkboxUnchecked = ColorPalette.grey200
  override val checkboxUncheckedDisabled = ColorPalette.grey400
  override val checkboxCheckmark = ColorPalette.blue900
  override val checkboxIndeterminateDisabled = ColorPalette.grey700

  override val preferenceBackground = Color.Transparent
  override val preferenceBackgroundDisabled = ColorPalette.black.copy(alpha = 0.35f)
  override val preferenceForeground = pageText
  override val preferenceForegroundDisabled = ColorPalette.grey400
  override val preferenceSubtitle = ColorPalette.grey400
  override val preferenceSubtitleDisabled = ColorPalette.grey600

  override val progressBarBackground = ColorPalette.grey600
  override val progressBarForeground = pageTextPrimary

  override val scrollbar = ColorPalette.blue400
  override val scrollbarSelected = ColorPalette.blue100

  override val sliderThumb = ColorPalette.blue400
  override val sliderActiveTrack = ColorPalette.blue800
  override val sliderActiveTick = ColorPalette.blue600
  override val sliderInactiveTrack = ColorPalette.grey700
  override val sliderInactiveTick = ColorPalette.grey700
}
