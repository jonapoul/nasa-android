package nasa.core.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import nasa.core.res.CoreStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme

@Composable
fun VideoOverlay(
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Image(
    modifier = modifier,
    imageVector = Icons.Filled.PlayArrow,
    contentDescription = CoreStrings.playVideo,
    colorFilter = ColorFilter.tint(theme.pageText),
    alpha = 0.3f,
  )
}
