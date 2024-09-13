package nasa.core.ui

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.LinearProgressIndicator
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nasa.core.model.Percent
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme

@Composable
fun FullscreenLoadableImage(
  request: ImageRequest,
  progress: Percent,
  contentDescription: String,
  modifier: Modifier = Modifier,
  minZoom: Float = 1f,
  maxZoom: Float = 10f,
  theme: Theme = LocalTheme.current,
) {
  var scale by remember { mutableFloatStateOf(1f) }
  FullscreenLoadableImage(
    request = request,
    progress = progress,
    contentDescription = contentDescription,
    scale = scale,
    onSetScale = { scale = it },
    modifier = modifier,
    minZoom = minZoom,
    maxZoom = maxZoom,
    theme = theme,
  )
}

/**
 * Adapted from https://www.youtube.com/watch?v=3CjOyoqi_PQ
 */
@Composable
fun FullscreenLoadableImage(
  request: ImageRequest,
  progress: Percent,
  contentDescription: String,
  scale: Float,
  onSetScale: (Float) -> Unit,
  modifier: Modifier = Modifier,
  minZoom: Float = 1f,
  maxZoom: Float = 10f,
  theme: Theme = LocalTheme.current,
  onSuccess: () -> Unit = {},
) {
  var offset by remember { mutableStateOf(Offset.Zero) }

  BoxWithConstraints(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
      onSetScale((scale * zoomChange).coerceIn(minZoom, maxZoom))

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
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(ShimmerBlockShape)
            .shimmer(theme, durationMillis = 3000),
        )

        VerticalSpacer(32.dp)

        LinearProgressIndicator(
          modifier = Modifier.fillMaxWidth(),
          color = theme.progressBarForeground,
          trackColor = theme.progressBarBackground,
          progress = { progress.toFraction() },
        )
      }
    }

    var imageModifier = Modifier.fillMaxSize()
    if (!isLoading) {
      imageModifier = imageModifier
        .graphicsLayer {
          scaleX = scale
          scaleY = scale
          translationX = offset.x
          translationY = offset.y
        }.transformable(transformableState)
    }

    AsyncImage(
      modifier = imageModifier,
      model = request,
      contentDescription = contentDescription,
      contentScale = ContentScale.Fit,
      alignment = Alignment.Center,
      fallback = rememberVectorPainter(Icons.Filled.Warning),
      onLoading = { isLoading = true },
      onError = { isLoading = false },
      onSuccess = {
        isLoading = false
        onSuccess()
      },
    )
  }
}
