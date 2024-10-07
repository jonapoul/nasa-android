package nasa.gallery.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import nasa.core.http.progress.DownloadProgressInterceptor
import nasa.core.model.Percent
import nasa.core.model.percent
import nasa.core.ui.LoadableImage
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Composable
internal fun ImageSuccess(
  url: String,
  progress: Percent,
  contentDescription: String,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val ctx = LocalContext.current
  val request = remember(url) {
    // Adding a custom header to the request, so our DownloadProgressInterceptor can identify it and track progress
    ImageRequest
      .Builder(ctx)
      .data(url)
      .crossfade(durationMillis = 200)
      .addHeader(DownloadProgressInterceptor.HEADER, IDENTIFIER)
      .build()
  }

  LoadableImage(
    modifier = modifier,
    request = request,
    progress = progress,
    contentDescription = contentDescription,
    minZoom = MIN_ZOOM,
    maxZoom = MAX_ZOOM,
    theme = theme,
  )
}

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 10f
private const val IDENTIFIER = "gallery"

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ImageSuccess(
    url = PREVIEW_URL,
    progress = 69.percent,
    contentDescription = "Hello world",
  )
}
