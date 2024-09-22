package nasa.core.ui.color

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import my.nanihadesuka.compose.ScrollbarLayoutSide
import my.nanihadesuka.compose.ScrollbarSettings

@Stable
@Composable
fun Theme.topAppBarColors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
  containerColor = toolbarBackground,
  titleContentColor = toolbarText,
  actionIconContentColor = toolbarButton,
  navigationIconContentColor = toolbarButton,
)

@Stable
@Composable
fun Theme.primaryButton(isPressed: Boolean) = ButtonDefaults.buttonColors(
  containerColor = if (isPressed) buttonPrimaryBackgroundSelected else buttonPrimaryBackground,
  disabledContainerColor = buttonPrimaryDisabledBackground,
  contentColor = if (isPressed) buttonPrimaryTextSelected else buttonPrimaryText,
  disabledContentColor = buttonPrimaryDisabledText,
)

@Stable
@Composable
fun Theme.regularButton(
  isPressed: Boolean,
  container: Color = buttonRegularBackground,
  containerPressed: Color = buttonRegularBackgroundSelected,
  containerDisabled: Color = buttonRegularDisabledBackground,
  text: Color = buttonRegularText,
  textPressed: Color = buttonRegularTextSelected,
  textDisabled: Color = buttonRegularDisabledText,
) = ButtonDefaults.buttonColors(
  containerColor = if (isPressed) containerPressed else container,
  disabledContainerColor = containerDisabled,
  contentColor = if (isPressed) textPressed else text,
  disabledContentColor = textDisabled,
)

@Stable
fun Theme.radioButton() = RadioButtonColors(
  selectedColor = pageTextPrimary,
  unselectedColor = pageTextPrimary,
  disabledSelectedColor = buttonPrimaryDisabledBackground,
  disabledUnselectedColor = buttonRegularDisabledBackground,
)

@Stable
@Composable
fun Theme.textField(
  focusedContainer: Color = formInputBackground,
  unfocusedContainer: Color = focusedContainer,
  text: Color = formInputText,
  textPlaceholder: Color = formInputTextPlaceholder,
  icon: Color = formInputText,
): TextFieldColors = TextFieldDefaults.colors(
  focusedTextColor = text,
  unfocusedTextColor = text,
  focusedPlaceholderColor = textPlaceholder,
  unfocusedPlaceholderColor = textPlaceholder,
  focusedLabelColor = textPlaceholder,
  unfocusedLabelColor = textPlaceholder,
  focusedIndicatorColor = Color.Transparent,
  unfocusedIndicatorColor = Color.Transparent,
  disabledIndicatorColor = Color.Transparent,
  focusedContainerColor = focusedContainer,
  unfocusedContainerColor = unfocusedContainer,
  cursorColor = text,
  focusedLeadingIconColor = icon,
  unfocusedLeadingIconColor = icon,
  focusedTrailingIconColor = icon,
  unfocusedTrailingIconColor = icon,
)

@Stable
@Composable
fun Theme.textFieldDialog(): TextFieldColors = textField(focusedContainer = formInputBackgroundDialog)

@Stable
@Composable
fun Theme.exposedDropDownMenu(): TextFieldColors = textField().copy(
  focusedTrailingIconColor = formInputText,
  unfocusedTrailingIconColor = formInputText,
)

@Stable
@Composable
fun Theme.dropDownMenuItem(): MenuItemColors = MenuDefaults.itemColors(
  textColor = menuItemText,
  leadingIconColor = menuItemText,
  trailingIconColor = menuItemText,
)

@Stable
@Composable
fun Theme.datePicker(): DatePickerColors = DatePickerDefaults.colors(
  containerColor = dialogBackground,
  titleContentColor = pageTextPrimary,
  headlineContentColor = pageTextPrimary,
  selectedDayContainerColor = buttonPrimaryBackground,
  selectedDayContentColor = buttonPrimaryText,
)

@Stable
@Composable
fun Theme.scrollbarSettings(): ScrollbarSettings = ScrollbarSettings(
  side = ScrollbarLayoutSide.End,
  thumbUnselectedColor = scrollbar,
  thumbSelectedColor = scrollbarSelected,
)

@Stable
@Composable
fun Theme.slider(): SliderColors = SliderDefaults.colors(
  thumbColor = sliderThumb,
  activeTrackColor = sliderActiveTrack,
  activeTickColor = sliderActiveTick,
  inactiveTrackColor = sliderInactiveTrack,
  inactiveTickColor = sliderInactiveTick,
)
