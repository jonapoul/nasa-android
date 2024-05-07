package nasa.core.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn

@Stable
@Composable
fun PrimaryIconButton(
  imageVector: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: Dp? = null,
  enabled: Boolean = true,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit = { DefaultIconButtonContent(imageVector, contentDescription, size) },
) {
  BasicIconButton(
    imageVector = imageVector,
    contentDescription = contentDescription,
    onClick = onClick,
    colors = { theme, isPressed -> theme.primary(isPressed) },
    modifier = modifier,
    size = size,
    enabled = enabled,
    shape = shape,
    interactionSource = interactionSource,
    content = content,
  )
}

@Stable
@Composable
fun RegularIconButton(
  imageVector: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: Dp? = null,
  enabled: Boolean = true,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit = { DefaultIconButtonContent(imageVector, contentDescription, size) },
) {
  BasicIconButton(
    imageVector = imageVector,
    contentDescription = contentDescription,
    onClick = onClick,
    colors = { theme, isPressed -> theme.regular(isPressed) },
    modifier = modifier,
    size = size,
    enabled = enabled,
    shape = shape,
    interactionSource = interactionSource,
    content = content,
  )
}

@Stable
@Composable
fun BareIconButton(
  imageVector: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: Dp? = null,
  enabled: Boolean = true,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit = { DefaultIconButtonContent(imageVector, contentDescription, size) },
) {
  BasicIconButton(
    imageVector = imageVector,
    contentDescription = contentDescription,
    onClick = onClick,
    colors = { theme, isPressed -> theme.bare(isPressed) },
    modifier = modifier,
    size = size,
    enabled = enabled,
    shape = shape,
    interactionSource = interactionSource,
    content = content,
  )
}

@Stable
@Composable
fun BasicIconButton(
  imageVector: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  colors: @Composable (theme: Theme, isPressed: Boolean) -> IconButtonColors,
  modifier: Modifier = Modifier,
  size: Dp? = null,
  enabled: Boolean = true,
  shape: Shape = DefaultButtonShape,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit = { DefaultIconButtonContent(imageVector, contentDescription, size) },
) {
  val theme = LocalTheme.current
  val isPressed by interactionSource.collectIsPressedAsState()
  val buttonColors = colors(theme, isPressed)
  val background = if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor

  IconButton(
    modifier = modifier.background(background, shape),
    onClick = onClick,
    enabled = enabled,
    colors = buttonColors,
    content = content,
  )
}

@Stable
@Composable
private fun DefaultIconButtonContent(
  imageVector: ImageVector,
  contentDescription: String,
  size: Dp? = null,
) {
  Icon(
    modifier = if (size == null) Modifier else Modifier.size(size),
    imageVector = imageVector,
    contentDescription = contentDescription,
  )
}

@Stable
@Composable
private fun Theme.primary(isPressed: Boolean) = IconButtonDefaults.filledIconButtonColors(
  containerColor = if (isPressed) buttonPrimaryBackgroundSelected else buttonPrimaryBackground,
  disabledContainerColor = buttonPrimaryDisabledBackground,
  contentColor = if (isPressed) buttonPrimaryTextSelected else buttonPrimaryText,
  disabledContentColor = buttonPrimaryDisabledText,
)

@Stable
@Composable
private fun Theme.regular(isPressed: Boolean) = IconButtonDefaults.filledIconButtonColors(
  containerColor = if (isPressed) buttonRegularBackgroundSelected else buttonRegularBackground,
  disabledContainerColor = buttonRegularDisabledBackground,
  contentColor = if (isPressed) buttonRegularTextSelected else buttonRegularText,
  disabledContentColor = buttonRegularDisabledText,
)

@Stable
@Composable
private fun Theme.bare(isPressed: Boolean) = IconButtonDefaults.filledIconButtonColors(
  containerColor = if (isPressed) buttonBareBackgroundSelected else buttonBareBackground,
  disabledContainerColor = buttonBareDisabledBackground,
  contentColor = if (isPressed) buttonBareTextSelected else buttonBareText,
  disabledContentColor = buttonBareDisabledText,
)

@Preview
@Composable
private fun Bare() = PreviewColumn {
  BareIconButton(
    imageVector = Icons.Filled.Check,
    contentDescription = "Cancel",
    onClick = {},
  )
}

@Preview
@Composable
private fun Regular() = PreviewColumn {
  RegularIconButton(
    imageVector = Icons.Filled.Info,
    contentDescription = "Cancel",
    onClick = {},
  )
}

@Preview
@Composable
private fun Primary() = PreviewColumn {
  PrimaryIconButton(
    imageVector = Icons.Filled.Check,
    contentDescription = "OK",
    onClick = {},
  )
}
