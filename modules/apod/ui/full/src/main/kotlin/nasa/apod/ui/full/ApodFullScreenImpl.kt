package nasa.apod.ui.full

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.apod.preview.PREVIEW_DATE
import nasa.apod.preview.PREVIEW_ITEM_1
import nasa.apod.res.ApodStrings
import nasa.core.http.progress.DownloadProgressInterceptor
import nasa.core.model.Percent
import nasa.core.model.percent
import nasa.core.res.CoreDimens
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.LoadableImage
import nasa.core.ui.button.RegularIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun ApodFullScreenImpl(
  date: LocalDate,
  item: ApodItem?,
  progress: Percent,
  onClickedBack: () -> Unit,
) {
  val theme = LocalTheme.current
  val hazeState = remember { HazeState() }
  val hazeStyle = HazeMaterials.ultraThin(theme.pageBackground)

  Scaffold(
    topBar = { ApodFullScreenTopBar(date, item, hazeState, hazeStyle, theme, onClickedBack) },
  ) { innerPadding ->
    item ?: return@Scaffold
    BackgroundSurface(theme = theme) {
      ApodFullScreenContent(
        modifier = Modifier.padding(innerPadding),
        item = item,
        progress = progress,
        hazeState = hazeState,
        theme = theme,
      )
    }
  }
}

@Composable
private fun ApodFullScreenContent(
  item: ApodItem,
  progress: Percent,
  hazeState: HazeState,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val ctx = LocalContext.current
  val request = remember(item) {
    // Adding a custom header to the request, so our DownloadProgressInterceptor can identify it and track progress
    ImageRequest
      .Builder(ctx)
      .data(item.hdUrl)
      .crossfade(durationMillis = 200)
      .addHeader(DownloadProgressInterceptor.HEADER, item.date.toString())
      .build()
  }

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomEnd,
  ) {
    var scale by remember { mutableFloatStateOf(1f) }
    var loaded by remember { mutableStateOf(false) }

    LoadableImage(
      modifier = Modifier
        .haze(hazeState)
        .fillMaxSize(),
      request = request,
      progress = progress,
      contentDescription = item.title,
      minZoom = MIN_ZOOM,
      maxZoom = MAX_ZOOM,
      theme = theme,
      scale = scale,
      onSetScale = { newScale -> scale = newScale },
      onSuccess = { loaded = true },
    )

    // Only show zoom buttons once the image has fully loaded
    if (loaded) {
      ZoomButtons(
        modifier = Modifier
          .wrapContentSize()
          .padding(CoreDimens.huge),
        scale = scale,
        onSetScale = { scale = it },
      )
    }
  }
}

@Stable
@Composable
private fun ZoomButtons(
  scale: Float,
  onSetScale: (Float) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    RegularIconButton(
      imageVector = Icons.Filled.ZoomIn,
      contentDescription = ApodStrings.fullscreenZoomIn,
      onClick = { onSetScale((scale * ZOOM_FACTOR).coerceIn(MIN_ZOOM, MAX_ZOOM)) },
    )

    VerticalSpacer(CoreDimens.small)

    RegularIconButton(
      imageVector = Icons.Filled.ZoomOut,
      contentDescription = ApodStrings.fullscreenZoomOut,
      onClick = { onSetScale((scale / ZOOM_FACTOR).coerceIn(MIN_ZOOM, MAX_ZOOM)) },
    )
  }
}

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 10f
private const val ZOOM_FACTOR = 1.2f

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ApodFullScreenImpl(
    date = PREVIEW_DATE,
    item = PREVIEW_ITEM_1,
    progress = 69.percent,
    onClickedBack = {},
  )
}
