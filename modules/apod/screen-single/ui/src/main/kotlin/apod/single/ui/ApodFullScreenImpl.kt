package apod.single.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import apod.core.model.ApodItem
import apod.core.model.ApodMediaType
import apod.core.ui.BackgroundSurface
import apod.core.ui.ShimmerBlockShape
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.core.ui.shimmer
import coil.compose.AsyncImage

@Composable
internal fun ApodFullScreenImpl(
  item: ApodItem,
  onClickedBack: () -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { ApodFullScreenTopBar(item, theme, onClickedBack) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      ApodFullScreenContent(
        modifier = Modifier.padding(innerPadding),
        item = item,
        theme = theme,
      )
    }
  }
}

@Composable
private fun ApodFullScreenContent(
  item: ApodItem,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    var isLoading by remember { mutableStateOf(true) }
    if (isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp)
          .clip(ShimmerBlockShape)
          .shimmer(theme, durationMillis = 3000),
      )
    }

    val fallback = rememberVectorPainter(Icons.Filled.Warning)
    AsyncImage(
      modifier = Modifier.fillMaxSize(),
      model = item.hdUrl,
      contentDescription = item.title,
      contentScale = ContentScale.Fit,
      alignment = Alignment.Center,
      fallback = fallback,
      onLoading = { isLoading = true },
      onSuccess = { isLoading = false },
      onError = { isLoading = false },
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ApodFullScreenImpl(
    item = EXAMPLE_ITEM,
    onClickedBack = {},
  )
}
