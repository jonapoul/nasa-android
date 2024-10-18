package nasa.core.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.Dimensions
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn

@Composable
fun ToggleableButton(
  text: String,
  isChecked: Boolean,
  icon: ImageVector,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
  mutableInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
  val isPressed by mutableInteractionSource.collectIsPressedAsState()

  val contentsColor = remember(isChecked, isPressed) {
    when {
      isChecked -> theme.pageTextPrimary
      isPressed -> theme.buttonPrimaryBackgroundSelected
      else -> theme.buttonRegularText
    }
  }

  Row(
    modifier = modifier
      .padding(vertical = Dimensions.Medium, horizontal = Dimensions.Small)
      .background(theme.buttonRegularBackground, BUTTON_SHAPE)
      .toggleableBorder(isChecked, isPressed, theme)
      .padding(6.dp)
      .clickable(mutableInteractionSource, ripple()) { onCheckedChange(!isChecked) },
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      modifier = Modifier.padding(Dimensions.Medium),
      imageVector = icon,
      contentDescription = null,
      tint = contentsColor,
    )

    Text(
      modifier = Modifier.padding(Dimensions.Medium),
      text = text,
      fontSize = 14.sp,
      color = contentsColor,
    )
  }
}

private val BUTTON_SHAPE = RoundedCornerShape(2.dp)

private fun Modifier.toggleableBorder(isChecked: Boolean, isPressed: Boolean, theme: Theme): Modifier {
  val borderColor = when {
    isChecked -> theme.pageTextPrimary
    isPressed -> theme.buttonPrimaryText
    else -> return this
  }

  return border(2.dp, borderColor, BUTTON_SHAPE)
}

@Preview
@Composable
private fun PreviewChecked() = PreviewColumn {
  ToggleableButton(
    text = "Hello",
    isChecked = true,
    icon = Icons.Filled.CheckCircle,
    onCheckedChange = {},
  )
}

@Preview
@Composable
private fun PreviewUnchecked() = PreviewColumn {
  ToggleableButton(
    text = "Hello",
    isChecked = false,
    icon = Icons.Filled.CropFree,
    onCheckedChange = {},
  )
}
