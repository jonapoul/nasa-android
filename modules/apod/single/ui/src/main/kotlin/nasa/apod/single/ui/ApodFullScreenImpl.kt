package nasa.apod.single.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import nasa.apod.model.ApodItem
import nasa.core.http.DOWNLOAD_IDENTIFIER_HEADER
import nasa.core.model.Percent
import nasa.core.model.percent
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.FullscreenLoadableImage
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Composable
internal fun ApodFullScreenImpl(
  item: ApodItem,
  progress: Percent,
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
        progress = progress,
        theme = theme,
      )
    }
  }
}

@Composable
private fun ApodFullScreenContent(
  item: ApodItem,
  progress: Percent,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val ctx = LocalContext.current
  val request = remember(item) {
    ImageRequest
      .Builder(ctx)
      .data(item.hdUrl)
      .crossfade(durationMillis = 200)
      .addHeader(DOWNLOAD_IDENTIFIER_HEADER, item.date.toString())
      .build()
  }

  FullscreenLoadableImage(
    modifier = modifier,
    request = request,
    progress = progress,
    contentDescription = item.title,
    minZoom = MIN_ZOOM,
    maxZoom = MAX_ZOOM,
    theme = theme,
  )
}

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 10f

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ApodFullScreenImpl(
    item = EXAMPLE_ITEM,
    progress = 69.percent,
    onClickedBack = {},
  )
}
