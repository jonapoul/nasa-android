package nasa.core.ui.text

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import nasa.core.res.R
import nasa.core.ui.CardShape
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.dropDownMenuItem
import nasa.core.ui.color.exposedDropDownMenu
import nasa.core.ui.color.textField
import nasa.core.ui.font.NasaFontFamily
import nasa.core.ui.preview.PreviewColumn

@Composable
fun NasaTextField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholderText: String?,
  modifier: Modifier = Modifier,
  shape: Shape = CardShape,
  readOnly: Boolean = false,
  label: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  colors: TextFieldColors? = null,
  clearable: Boolean = false,
  theme: Theme = LocalTheme.current,
) {
  val isFocused by interactionSource.collectIsFocusedAsState()
  var fieldModifier = modifier
  if (isFocused) {
    fieldModifier = fieldModifier.shadow(4.dp, shape, ambientColor = theme.formInputShadow)
  }

  val clearButton: (@Composable () -> Unit)? = if (clearable && value.isNotEmpty()) {
    {
      ClearButton(
        tint = colors?.focusedTrailingIconColor ?: theme.pageText,
        onClick = { onValueChange("") },
      )
    }
  } else {
    null
  }

  TextField(
    modifier = fieldModifier,
    value = value,
    placeholder = if (placeholderText == null) {
      null
    } else {
      { Text(text = placeholderText, fontFamily = NasaFontFamily) }
    },
    shape = shape,
    colors = colors ?: theme.textField(),
    readOnly = readOnly,
    label = label,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon ?: clearButton,
    interactionSource = interactionSource,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    onValueChange = onValueChange,
  )
}

@Composable
fun NasaExposedDropDownMenu(
  value: String,
  onValueChange: (String) -> Unit,
  options: ImmutableList<String>,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedOptionText by remember { mutableStateOf(value) }

  ExposedDropdownMenuBox(
    modifier = modifier,
    expanded = expanded,
    onExpandedChange = { expanded = it },
  ) {
    NasaTextField(
      modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
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

@Composable
private fun ClearButton(
  tint: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  IconButton(
    modifier = modifier,
    onClick = onClick,
  ) {
    Icon(
      imageVector = Icons.Default.Clear,
      contentDescription = stringResource(id = R.string.input_clear),
      tint = tint,
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyTextField() = PreviewColumn {
  NasaTextField(
    value = "",
    onValueChange = {},
    placeholderText = "I'm empty",
  )
}

@Preview
@Composable
private fun PreviewFilledTextField() = PreviewColumn {
  NasaTextField(
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
  NasaExposedDropDownMenu(
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
  NasaExposedDropDownMenu(
    modifier = Modifier.width(100.dp),
    value = value,
    onValueChange = { value = it },
    options = options,
  )
}
