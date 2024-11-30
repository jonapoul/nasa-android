package nasa.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun StarryBackground(
  density: Float,
  minSize: Float,
  maxSize: Float,
  blur: Blur,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Canvas(modifier = modifier) {
    val random = Random(System.currentTimeMillis())
    val starCount = (density * size.width * size.height).roundToInt()

    repeat(starCount) {
      val x = random.nextFloat() * size.width
      val y = random.nextFloat() * size.height
      val starSize = random.nextFloat() * (maxSize - minSize) + minSize

      drawIntoCanvas { canvas ->
        translate(left = x, top = y) {
          val paint = Paint().apply {
            color = theme.backgroundStar
            asFrameworkPaint().apply {
              setShadowLayer(blur.radius, 0f, 0f, theme.backgroundStar.toArgb())
            }
          }

          canvas.drawCircle(
            radius = starSize / 2f,
            center = Offset(0f, 0f),
            paint = paint,
          )
        }
      }
    }
  }
}

@Immutable
enum class Blur(val radius: Float) {
  None(radius = 0f),
  Low(radius = 2.5f),
  Medium(radius = 5f),
  High(radius = 7.5f),
  Max(radius = 10f),
}

private val PREVIEW_SIZE = Modifier.size(50.dp, 50.dp)
private const val PREVIEW_DENSITY = 8e-4f
private const val PREVIEW_MIN_SIZE = 0.5f
private const val PREVIEW_MAX_SIZE = 5f

@Preview
@Composable
private fun PreviewNoBlur() = PreviewColumn {
  StarryBackground(PREVIEW_DENSITY, PREVIEW_MIN_SIZE, PREVIEW_MAX_SIZE, Blur.None, PREVIEW_SIZE)
}

@Preview
@Composable
private fun PreviewLowBlur() = PreviewColumn {
  StarryBackground(PREVIEW_DENSITY, PREVIEW_MIN_SIZE, PREVIEW_MAX_SIZE, Blur.Low, PREVIEW_SIZE)
}

@Preview
@Composable
private fun PreviewMediumBlur() = PreviewColumn {
  StarryBackground(PREVIEW_DENSITY, PREVIEW_MIN_SIZE, PREVIEW_MAX_SIZE, Blur.Medium, PREVIEW_SIZE)
}

@Preview
@Composable
private fun PreviewHighBlur() = PreviewColumn {
  StarryBackground(PREVIEW_DENSITY, PREVIEW_MIN_SIZE, PREVIEW_MAX_SIZE, Blur.High, PREVIEW_SIZE)
}

@Preview
@Composable
private fun PreviewMaxBlur() = PreviewColumn {
  StarryBackground(PREVIEW_DENSITY, PREVIEW_MIN_SIZE, PREVIEW_MAX_SIZE, Blur.Max, PREVIEW_SIZE)
}
