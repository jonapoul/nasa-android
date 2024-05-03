package apod.core.ui.text

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import apod.core.ui.CardShape
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.dropDownMenuItem
import apod.core.ui.color.exposedDropDownMenu
import apod.core.ui.color.textField
import apod.core.ui.font.ApodFontFamily
import apod.core.ui.preview.PreviewColumn
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ApodTextField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholderText: String?,
  modifier: Modifier = Modifier,
  shape: Shape = CardShape,
  readOnly: Boolean = false,
  trailingIcon: @Composable (() -> Unit)? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  colors: TextFieldColors? = null,
) {
  val theme = LocalTheme.current
  val isFocused by interactionSource.collectIsFocusedAsState()
  var fieldModifier = modifier
  if (isFocused) {
    fieldModifier = fieldModifier.shadow(4.dp, shape, ambientColor = theme.formInputShadow)
  }

  TextField(
    modifier = fieldModifier,
    value = value,
    placeholder = if (placeholderText == null) {
      null
    } else {
      { Text(text = placeholderText, fontFamily = ApodFontFamily) }
    },
    shape = shape,
    colors = colors ?: theme.textField(),
    readOnly = readOnly,
    trailingIcon = trailingIcon,
    interactionSource = interactionSource,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    onValueChange = onValueChange,
  )
}

@Composable
fun ApodExposedDropDownMenu(
  value: String,
  onValueChange: (String) -> Unit,
  options: ImmutableList<String>,
  modifier: Modifier = Modifier,
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedOptionText by remember { mutableStateOf(value) }
  val theme = LocalTheme.current

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = it },
  ) {
    ApodTextField(
      modifier = modifier.menuAnchor(),
      readOnly = true,
      placeholderText = null,
      value = selectedOptionText,
      onValueChange = onValueChange,
      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
      colors = theme.exposedDropDownMenu(),
    )

    ExposedDropdownMenu(
      modifier = Modifier.background(theme.menuItemBackground),
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      val itemColors = theme.dropDownMenuItem()
      options.fastForEach { selectionOption ->
        DropdownMenuItem(
          text = { Text(selectionOption) },
          contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
          colors = itemColors,
          onClick = {
            selectedOptionText = selectionOption
            onValueChange(selectionOption)
            expanded = false
          },
        )
      }
    }
  }
}

@Preview
@Composable
private fun PreviewEmptyTextField() = PreviewColumn {
  ApodTextField(
    value = "",
    onValueChange = {},
    placeholderText = "I'm empty",
  )
}

@Preview
@Composable
private fun PreviewFilledTextField() = PreviewColumn {
  ApodTextField(
    value = "I'm full",
    onValueChange = {},
    placeholderText = "Hello world",
  )
}

@Preview
@Composable
private fun PreviewDropDownMenu() = PreviewColumn {
  var value by remember { mutableStateOf("B") }
  val options = persistentListOf("A", "B", "C", "D")
  ApodExposedDropDownMenu(
    value = value,
    onValueChange = { value = it },
    options = options,
  )
}

@Preview
@Composable
private fun PreviewDropDownMenuForcedWidth() = PreviewColumn {
  var value by remember { mutableStateOf("B") }
  val options = persistentListOf("A", "B", "C", "D")
  ApodExposedDropDownMenu(
    modifier = Modifier.width(100.dp),
    value = value,
    onValueChange = { value = it },
    options = options,
  )
}
