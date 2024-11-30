package nasa.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeChildScope
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun StarryBackground(
  modifier: Modifier = Modifier,
  config: StarryBackgroundConfig = StarryBackgroundConfig.Default,
  theme: Theme = LocalTheme.current,
  seed: Long = remember { System.currentTimeMillis() },
) {
  val hazeState = remember { HazeState() }

  Box(modifier = modifier) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .haze(hazeState),
    ) {
      val random = Random(seed)
      val paint = Paint().apply { color = theme.backgroundStar }
      val starCount = (size.width * size.height * config.density).roundToInt()

      repeat(starCount) {
        val x = random.nextFloat() * size.width
        val y = random.nextFloat() * size.height
        val starSize = random.nextFloat() * (config.maxSize - config.minSize) + config.minSize

        drawIntoCanvas { canvas ->
          translate(left = x, top = y) {
            canvas.drawCircle(
              radius = starSize / 2f,
              center = Offset(x = 0f, y = 0f),
              paint = paint,
            )
          }
        }
      }
    }

    Box(
      modifier = Modifier
        .hazeChild(hazeState) { setStyle(theme, config.blur) }
        .fillMaxSize(),
    )
  }
}

/**
 * [density] is in units of "stars per square [dp]".
 */
@Immutable
data class StarryBackgroundConfig(
  val density: Float = 0.0001f,
  val minSize: Float = 1f,
  val maxSize: Float = 10f,
  val blur: StarryBlur = StarryBlur.Medium,
) {
  companion object {
    val Default = StarryBackgroundConfig()
  }
}

@Immutable
enum class StarryBlur { None, Low, Medium, High, Max, }

@Stable
private fun HazeChildScope.setStyle(theme: Theme, blur: StarryBlur) {
  backgroundColor = theme.pageBackground
  style = HazeStyle(
    backgroundColor = Color.Transparent,
    tints = emptyList(),
    blurRadius = when (blur) {
      StarryBlur.None -> 0.dp
      StarryBlur.Low -> 2.dp
      StarryBlur.Medium -> 4.dp
      StarryBlur.High -> 6.dp
      StarryBlur.Max -> 8.dp
    },
  )
}

@Preview
@Composable
private fun PreviewNoBlur() = PreviewStarryBackground(StarryBlur.None)

@Preview
@Composable
private fun PreviewLowBlur() = PreviewStarryBackground(StarryBlur.Low)

@Preview
@Composable
private fun PreviewMediumBlur() = PreviewStarryBackground(StarryBlur.Medium)

@Preview
@Composable
private fun PreviewHighBlur() = PreviewStarryBackground(StarryBlur.High)

@Preview
@Composable
private fun PreviewMaxBlur() = PreviewStarryBackground(StarryBlur.Max)

@Composable
private fun PreviewStarryBackground(blur: StarryBlur) = PreviewColumn {
  StarryBackground(
    modifier = Modifier.size(200.dp, 200.dp),
    config = StarryBackgroundConfig(
      density = 5e-4f,
      minSize = 0.5f,
      maxSize = 10f,
      blur = blur,
    ),
  )
}
