package apod.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import apod.core.ui.color.Theme

@Composable
fun BackgroundSurface(
  theme: Theme,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Surface(
    modifier = modifier
      .fillMaxSize()
      .background(theme.pageBackground),
    content = content,
  )
}
