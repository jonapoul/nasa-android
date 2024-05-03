package apod.core.ui.color

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

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
@Composable
fun Theme.bareButton(isPressed: Boolean) = ButtonDefaults.buttonColors(
  containerColor = if (isPressed) buttonBareBackgroundSelected else buttonBareBackground,
  disabledContainerColor = buttonBareDisabledBackground,
  contentColor = if (isPressed) buttonBareTextSelected else buttonBareText,
  disabledContentColor = buttonBareDisabledText,
)

@Stable
fun Theme.radioButton() = RadioButtonColors(
  selectedColor = buttonPrimaryBackground,
  unselectedColor = pageText,
  disabledSelectedColor = buttonPrimaryDisabledBackground,
  disabledUnselectedColor = buttonRegularDisabledBackground,
)

@Stable
@Composable
fun Theme.textField(): TextFieldColors = TextFieldDefaults.colors(
  focusedTextColor = formInputText,
  unfocusedTextColor = formInputText,
  focusedPlaceholderColor = formInputTextPlaceholder,
  unfocusedPlaceholderColor = formInputTextPlaceholder,
  focusedIndicatorColor = Color.Transparent,
  unfocusedIndicatorColor = Color.Transparent,
  disabledIndicatorColor = Color.Transparent,
  focusedContainerColor = formInputBackground,
  unfocusedContainerColor = formInputBackground,
  cursorColor = formInputText,
)

@Stable
@Composable
fun Theme.exposedDropDownMenu(): TextFieldColors = textField().copy(
  focusedTrailingIconColor = formInputText,
  unfocusedTrailingIconColor = formInputText,
)

@Stable
@Composable
fun Theme.dropDownMenuItem(): MenuItemColors = MenuDefaults.itemColors().copy(
  textColor = formInputText,
)
