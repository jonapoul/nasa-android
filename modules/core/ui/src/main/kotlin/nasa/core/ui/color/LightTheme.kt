package nasa.core.ui.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal class LightTheme : Theme {
  override val pageBackground = ColorPalette.navy100
  override val pageBackgroundAlt = ColorPalette.navy150
  override val pageText = Color(color = 0xFF272630)
  override val pageTextSubdued = ColorPalette.grey400
  override val pageTextPrimary = ColorPalette.blue600
  override val pageTextLoading = ColorPalette.grey500

  override val backgroundStar = ColorPalette.black

  override val cardBackground = ColorPalette.white
  override val cardShadow = ColorPalette.navy700

  override val toolbarBackground = ColorPalette.grey700
  override val toolbarBackgroundSubdued = ColorPalette.grey600
  override val toolbarText = ColorPalette.white
  override val toolbarTextSubdued = ColorPalette.grey200
  override val toolbarButton = ColorPalette.white

  override val menuItemBackground = ColorPalette.navy50
  override val menuItemText = ColorPalette.navy900

  override val dialogBackground = ColorPalette.white
  override val dialogProgressWheelTrack = ColorPalette.grey100

  override val buttonPrimaryText = Color.White
  override val buttonPrimaryTextSelected = buttonPrimaryText
  override val buttonPrimaryBackground = ColorPalette.blue500
  override val buttonPrimaryBackgroundSelected = ColorPalette.blue300
  override val buttonPrimaryDisabledText = ColorPalette.grey300
  override val buttonPrimaryDisabledBackground = ColorPalette.grey200

  override val buttonRegularText = ColorPalette.black
  override val buttonRegularTextSelected = ColorPalette.navy900
  override val buttonRegularBackground = ColorPalette.navy200
  override val buttonRegularBackgroundSelected = ColorPalette.grey600
  override val buttonRegularDisabledText = ColorPalette.navy300
  override val buttonRegularDisabledBackground = ColorPalette.white

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

  override val formInputBackground = ColorPalette.navy50
  override val formInputBackgroundDialog = ColorPalette.navy100
  override val formInputShadow = ColorPalette.blue300
  override val formInputText = ColorPalette.navy900
  override val formInputTextPlaceholder = ColorPalette.navy300

  override val checkboxChecked = ColorPalette.blue700
  override val checkboxCheckedDisabled = ColorPalette.grey300
  override val checkboxUnchecked = ColorPalette.grey600
  override val checkboxUncheckedDisabled = ColorPalette.grey300
  override val checkboxCheckmark = ColorPalette.white
  override val checkboxIndeterminateDisabled = ColorPalette.grey700

  override val preferenceBackground = Color.Transparent
  override val preferenceBackgroundDisabled = ColorPalette.black.copy(alpha = 0.15f)
  override val preferenceForeground = pageText
  override val preferenceForegroundDisabled = ColorPalette.grey400
  override val preferenceSubtitle = ColorPalette.grey400
  override val preferenceSubtitleDisabled = ColorPalette.grey300

  override val progressBarBackground = ColorPalette.grey200
  override val progressBarForeground = pageTextPrimary

  override val scrollbar = ColorPalette.blue900
  override val scrollbarSelected = ColorPalette.blue400

  override val sliderThumb = ColorPalette.blue500
  override val sliderActiveTrack = ColorPalette.blue200
  override val sliderActiveTick = ColorPalette.blue400
  override val sliderInactiveTrack = ColorPalette.grey200
  override val sliderInactiveTick = ColorPalette.grey400
}
