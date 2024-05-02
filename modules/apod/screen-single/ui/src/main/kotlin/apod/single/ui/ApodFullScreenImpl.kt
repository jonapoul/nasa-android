package apod.single.ui

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import apod.core.model.ApodItem
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

/**
 * Adapted from https://www.youtube.com/watch?v=3CjOyoqi_PQ
 */
@Composable
private fun ApodFullScreenContent(
  item: ApodItem,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  var scale by remember { mutableFloatStateOf(1f) }
  var offset by remember { mutableStateOf(Offset.Zero) }

  BoxWithConstraints(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    val state = rememberTransformableState { zoomChange, panChange, _ ->
      scale = (scale * zoomChange).coerceIn(MIN_ZOOM, MAX_ZOOM)

      val extraWidth = (scale - 1) * constraints.maxWidth
      val extraHeight = (scale - 1) * constraints.maxHeight

      val maxX = extraWidth / 2
      val maxY = extraHeight / 2

      offset = Offset(
        x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
        y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
      )
    }

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

    var imageModifier = Modifier.fillMaxSize()
    if (!isLoading) {
      imageModifier = imageModifier
        .graphicsLayer {
          scaleX = scale
          scaleY = scale
          translationX = offset.x
          translationY = offset.y
        }
        .transformable(state)
    }

    AsyncImage(
      modifier = imageModifier,
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

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 10f

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ApodFullScreenImpl(
    item = EXAMPLE_ITEM,
    onClickedBack = {},
  )
}