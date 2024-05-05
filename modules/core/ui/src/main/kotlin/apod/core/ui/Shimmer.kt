package apod.core.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewColumn
import kotlinx.collections.immutable.persistentListOf

private const val DEFAULT_DURATION_MS = 1000

// Adapted from https://proandroiddev.com/shimmer-shadow-loading-effect-animation-with-jetpack-compose-f4b3de28dc2b
@SuppressLint("ComposeModifierComposed")
fun Modifier.shimmer(
  theme: Theme,
  color: Theme.() -> Color = { pageTextLoading },
  widthOfShadowBrush: Int = 500,
  angleOfAxisY: Float = 270f,
  durationMillis: Int = DEFAULT_DURATION_MS,
): Modifier {
  return composed {
    val baseColor = theme.color()
    val shimmerColors = persistentListOf(
      baseColor.copy(alpha = 0.1f),
      baseColor.copy(alpha = 0.2f),
      baseColor.copy(alpha = 0.5f),
      baseColor.copy(alpha = 0.2f),
      baseColor.copy(alpha = 0.1f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
      initialValue = 0f,
      targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
      animationSpec = infiniteRepeatable(
        animation = tween(durationMillis, easing = LinearEasing),
        repeatMode = RepeatMode.Restart,
      ),
      label = "Shimmer loading animation",
    )

    background(
      brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
      ),
    )
  }
}

@Composable
fun ShimmeringBlock(
  modifier: Modifier = Modifier,
  shape: Shape = ShimmerBlockShape,
  theme: Theme = LocalTheme.current,
  color: Theme.() -> Color = { pageTextLoading },
  durationMillis: Int = DEFAULT_DURATION_MS,
) {
  Box(
    modifier = modifier
      .clip(shape)
      .height(30.dp)
      .shimmer(theme, color, durationMillis = durationMillis),
  )
}

@Preview
@Composable
private fun PreviewSmall() = PreviewColumn {
  ShimmeringBlock(
    modifier = Modifier.width(50.dp),
  )
}

@Preview
@Composable
private fun PreviewLong() = PreviewColumn {
  ShimmeringBlock(
    modifier = Modifier.width(200.dp),
  )
}

@Preview
@Composable
private fun PreviewPositive() = PreviewColumn {
  val theme = LocalTheme.current
  ShimmeringBlock(
    modifier = Modifier
      .width(200.dp)
      .background(theme.pageBackground),
    color = { pageTextPrimary },
  )
}

@Preview
@Composable
private fun PreviewBig() = PreviewColumn {
  ShimmeringBlock(
    modifier = Modifier
      .fillMaxWidth()
      .height(50.dp),
  )
}
