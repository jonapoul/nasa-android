package apod.core.ui.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.font.ApodFontFamily
import apod.core.ui.font.style
import apod.core.ui.preview.PreviewColumn

@Stable
@Composable
fun PrimaryTextButton(
  text: String,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  contentPadding: PaddingValues = DefaultButtonPadding,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  style: TextStyle = ButtonPrimaryTextStyle,
  fontSize: TextUnit = TextUnit.Unspecified,
  prefix: (@Composable () -> Unit)? = null,
  colors: @Composable (Theme, Boolean) -> ButtonColors = { theme, pressed -> theme.primary(pressed) },
  content: @Composable RowScope.() -> Unit = { DefaultTextButtonContent(text, style, fontSize, prefix) },
  onClick: () -> Unit,
) {
  BasicTextButton(
    text = text,
    modifier = modifier,
    isEnabled = isEnabled,
    contentPadding = contentPadding,
    shape = shape,
    interactionSource = interactionSource,
    style = style,
    fontSize = fontSize,
    prefix = prefix,
    onClick = onClick,
    colors = colors,
    content = content,
  )
}

@Stable
@Composable
fun RegularTextButton(
  text: String,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  contentPadding: PaddingValues = DefaultButtonPadding,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  style: TextStyle = ButtonRegularTextStyle,
  fontSize: TextUnit = TextUnit.Unspecified,
  prefix: (@Composable () -> Unit)? = null,
  colors: @Composable (Theme, Boolean) -> ButtonColors = { theme, pressed -> theme.regular(pressed) },
  content: @Composable RowScope.() -> Unit = { DefaultTextButtonContent(text, style, fontSize, prefix) },
  onClick: () -> Unit,
) {
  BasicTextButton(
    text = text,
    modifier = modifier,
    isEnabled = isEnabled,
    contentPadding = contentPadding,
    shape = shape,
    interactionSource = interactionSource,
    style = style,
    fontSize = fontSize,
    prefix = prefix,
    onClick = onClick,
    colors = colors,
    content = content,
  )
}

@Stable
@Composable
fun PrimaryTextButtonWithLoading(
  text: String,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  contentPadding: PaddingValues = DefaultButtonPadding,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  style: TextStyle = ButtonPrimaryTextStyle,
  fontSize: TextUnit = TextUnit.Unspecified,
  prefix: (@Composable () -> Unit)? = null,
  colors: @Composable (Theme, Boolean) -> ButtonColors = { theme, pressed -> theme.primary(pressed) },
  onClick: () -> Unit,
) {
  PrimaryTextButton(
    text = text,
    modifier = modifier,
    isEnabled = isEnabled && !isLoading,
    contentPadding = contentPadding,
    shape = shape,
    interactionSource = interactionSource,
    style = style,
    fontSize = fontSize,
    prefix = prefix,
    colors = colors,
    onClick = onClick,
    content = {
      // Using opacity here so we don't adjust the size bounds of the containing box
      Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
          modifier = Modifier
            .alpha(if (isLoading) 1f else 0f)
            .size(20.dp),
          color = LocalTheme.current.buttonPrimaryText,
          strokeWidth = 2.dp,
        )

        Text(
          modifier = Modifier.alpha(if (isLoading) 0f else 1f),
          text = text,
          fontFamily = ApodFontFamily,
          style = style,
          fontSize = fontSize,
        )
      }
    },
  )
}

@Stable
@Composable
fun BareTextButton(
  text: String,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  contentPadding: PaddingValues = DefaultButtonPadding,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  style: TextStyle = ButtonBareTextStyle,
  fontSize: TextUnit = TextUnit.Unspecified,
  prefix: (@Composable () -> Unit)? = null,
  colors: @Composable (Theme, Boolean) -> ButtonColors = { theme, pressed -> theme.bare(pressed) },
  content: @Composable RowScope.() -> Unit = { DefaultTextButtonContent(text, style, fontSize, prefix) },
  onClick: () -> Unit,
) {
  BasicTextButton(
    text = text,
    modifier = modifier,
    isEnabled = isEnabled,
    contentPadding = contentPadding,
    shape = shape,
    interactionSource = interactionSource,
    style = style,
    fontSize = fontSize,
    prefix = prefix,
    onClick = onClick,
    colors = colors,
    content = content,
  )
}

@Stable
@Composable
fun BasicTextButton(
  text: String,
  colors: @Composable (theme: Theme, isPressed: Boolean) -> ButtonColors,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  contentPadding: PaddingValues = DefaultButtonPadding,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  style: TextStyle = LocalTextStyle.current,
  fontSize: TextUnit = TextUnit.Unspecified,
  prefix: (@Composable () -> Unit)? = null,
  content: @Composable RowScope.() -> Unit = { DefaultTextButtonContent(text, style, fontSize, prefix) },
  onClick: () -> Unit,
) {
  val theme = LocalTheme.current
  val isPressed by interactionSource.collectIsPressedAsState()

  TextButton(
    modifier = modifier.widthIn(min = 1.dp),
    enabled = isEnabled,
    shape = shape,
    colors = colors(theme, isPressed),
    contentPadding = contentPadding,
    interactionSource = interactionSource,
    onClick = onClick,
    content = { content() },
  )
}

@Stable
@Composable
private fun RowScope.DefaultTextButtonContent(
  text: String,
  style: TextStyle,
  fontSize: TextUnit,
  prefix: (@Composable () -> Unit)?,
) {
  prefix?.invoke()

  Text(
    text = text,
    fontFamily = ApodFontFamily,
    style = style,
    fontSize = fontSize,
  )
}

@Stable
@Composable
private fun Theme.primary(isPressed: Boolean) = ButtonDefaults.buttonColors(
  containerColor = if (isPressed) buttonPrimaryBackgroundSelected else buttonPrimaryBackground,
  disabledContainerColor = buttonPrimaryDisabledBackground,
  contentColor = if (isPressed) buttonPrimaryTextSelected else buttonPrimaryText,
  disabledContentColor = buttonPrimaryDisabledText,
)

@Stable
@Composable
fun Theme.regular(
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
private fun Theme.bare(isPressed: Boolean) = ButtonDefaults.buttonColors(
  containerColor = if (isPressed) buttonBareBackgroundSelected else buttonBareBackground,
  disabledContainerColor = buttonBareDisabledBackground,
  contentColor = if (isPressed) buttonBareTextSelected else buttonBareText,
  disabledContentColor = buttonBareDisabledText,
)

private val ButtonPrimaryTextStyle: TextStyle
  @Composable
  @ReadOnlyComposable
  get() = ApodFontFamily.style(fontSize = 15.sp, color = LocalTheme.current.buttonPrimaryText)

private val ButtonRegularTextStyle: TextStyle
  @Composable
  @ReadOnlyComposable
  get() = ApodFontFamily.style(fontSize = 15.sp, color = LocalTheme.current.buttonRegularText)

private val ButtonBareTextStyle: TextStyle
  @Composable
  @ReadOnlyComposable
  get() = ApodFontFamily.style(fontSize = 15.sp, color = LocalTheme.current.buttonBareText)

@Preview
@Composable
private fun PreviewBareButton() = PreviewColumn {
  BareTextButton(
    text = "Cancel",
    onClick = {},
  )
}

@Preview
@Composable
private fun PreviewRegularButton() = PreviewColumn {
  RegularTextButton(
    text = "Submit",
    onClick = {},
  )
}

@Preview
@Composable
private fun PreviewPrimaryButton() = PreviewColumn {
  PrimaryTextButton(
    text = "OK",
    onClick = {},
  )
}

@Preview
@Composable
private fun PreviewPrimaryWithLoadingNotLoadingButton() = PreviewColumn {
  PrimaryTextButtonWithLoading(
    text = "OK",
    isLoading = false,
    onClick = {},
  )
}

@Preview
@Composable
private fun PreviewPrimaryWithLoadingButton() = PreviewColumn {
  PrimaryTextButtonWithLoading(
    text = "OK",
    isLoading = true,
    onClick = {},
  )
}
