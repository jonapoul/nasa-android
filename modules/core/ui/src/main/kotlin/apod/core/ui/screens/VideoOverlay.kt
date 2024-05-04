package apod.core.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import apod.core.model.ApodItem
import apod.core.model.ApodMediaType
import apod.core.res.R
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme

@Composable
fun VideoOverlay(
  item: ApodItem,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  if (item.mediaType == ApodMediaType.Video) {
    // Video overlay
    Image(
      modifier = modifier,
      imageVector = Icons.Filled.PlayArrow,
      contentDescription = stringResource(R.string.play_video),
      colorFilter = ColorFilter.tint(theme.pageText),
      alpha = 0.3f,
    )
  }
}
