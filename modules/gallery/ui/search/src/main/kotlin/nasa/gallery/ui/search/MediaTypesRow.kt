package nasa.gallery.ui.search

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.res.R

@Composable
internal fun MediaTypesRow(
  mediaTypes: MediaTypes,
  onChange: (MediaTypes) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.Top,
  ) {
    if (mediaTypes == MediaTypes.Empty) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          modifier = Modifier.size(WARNING_HEIGHT),
          imageVector = Icons.Filled.Warning,
          contentDescription = null,
          tint = theme.warningText,
        )

        HorizontalSpacer(4.dp)

        Text(
          text = stringResource(R.string.search_modal_no_media_selected),
          textAlign = TextAlign.End,
          color = theme.warningText,
          fontSize = 11.sp,
        )
      }
    }

    Row {
      val types = remember(mediaTypes) { mediaTypes.toSet() }

      MediaTypeButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_image),
        icon = Icons.Filled.Image,
        isChecked = MediaType.Image in types,
        onCheckedChange = { checked -> submitTypesOnChange(checked, types, MediaType.Image, onChange) },
        theme = theme,
      )
      MediaTypeButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_video),
        icon = Icons.Filled.VideoLibrary,
        isChecked = MediaType.Video in types,
        onCheckedChange = { checked -> submitTypesOnChange(checked, types, MediaType.Video, onChange) },
        theme = theme,
      )
      MediaTypeButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_audio),
        icon = Icons.Filled.Audiotrack,
        isChecked = MediaType.Audio in types,
        onCheckedChange = { checked -> submitTypesOnChange(checked, types, MediaType.Audio, onChange) },
        theme = theme,
      )
    }
  }
}

@Composable
private fun MediaTypeButton(
  text: String,
  isChecked: Boolean,
  icon: ImageVector,
  onCheckedChange: (Boolean) -> Unit,
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  val mutableInteractionSource = remember { MutableInteractionSource() }
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
      .padding(vertical = 4.dp, horizontal = 2.dp)
      .background(theme.buttonRegularBackground, BUTTON_SHAPE)
      .mediaTypeBorder(isChecked, isPressed, theme)
      .padding(6.dp)
      .clickable(mutableInteractionSource, ripple()) { onCheckedChange(!isChecked) },
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      modifier = Modifier.padding(4.dp),
      imageVector = icon,
      contentDescription = null,
      tint = contentsColor,
    )

    Text(
      modifier = Modifier.padding(4.dp),
      text = text,
      fontSize = 14.sp,
      color = contentsColor,
    )
  }
}

private val BUTTON_SHAPE = RoundedCornerShape(2.dp)
private val WARNING_HEIGHT = 15.dp

private fun Modifier.mediaTypeBorder(isChecked: Boolean, isPressed: Boolean, theme: Theme): Modifier {
  val borderColor = when {
    isChecked -> theme.pageTextPrimary
    isPressed -> theme.buttonPrimaryText
    else -> return this
  }

  return border(2.dp, borderColor, BUTTON_SHAPE)
}

private fun submitTypesOnChange(
  checked: Boolean,
  mediaTypes: Set<MediaType>,
  type: MediaType,
  onChange: (MediaTypes) -> Unit,
) {
  val newTypes = if (checked) mediaTypes + type else mediaTypes - type
  onChange(MediaTypes(newTypes))
}

@Preview
@Composable
private fun PreviewDefault() = PreviewColumn {
  MediaTypesRow(
    mediaTypes = MediaTypes.All,
    onChange = {},
  )
}

@Preview
@Composable
private fun PreviewCustom() = PreviewColumn {
  MediaTypesRow(
    mediaTypes = MediaTypes(MediaType.Audio),
    onChange = {},
  )
}

@Preview
@Composable
private fun PreviewNone() = PreviewColumn {
  MediaTypesRow(
    mediaTypes = MediaTypes.Empty,
    onChange = {},
  )
}
