package nasa.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeChildScope
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
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
  val random = remember(seed) { Random(seed) }
  val particles = remember { mutableStateListOf<Particle>() }

  BoxWithConstraints(modifier = modifier) {
    DisposableEffect(constraints, random, config) {
      val starCount = (constraints.maxWidth * constraints.maxHeight * config.density).roundToInt()
      repeat(starCount) { particles.add(generateParticle(constraints, random, config)) }
      onDispose { particles.clear() }
    }

    particles.forEachIndexed { index, particle ->
      ParticleAnimation(
        constraints = constraints,
        particle = particle,
        theme = theme,
        onDisappear = {
          particles.removeAt(index)
          particles.add(generateParticle(constraints, random, config))
        },
      )
    }
  }
}

@Composable
private fun ParticleAnimation(
  constraints: Constraints,
  particle: Particle,
  onDisappear: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val infiniteTransition = rememberInfiniteTransition(label = "infinite")

  val x by infiniteTransition.animateFloat(
    label = "x",
    initialValue = particle.startX,
    targetValue = particle.startX + particle.velocityX * 300f, // Assume 300 frames of movement
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
  )

  val y by infiniteTransition.animateFloat(
    label = "y",
    initialValue = particle.startY,
    targetValue = particle.startY + particle.velocityY * 300f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
  )

  val xRange = remember(constraints) { 0f..constraints.maxWidth.toFloat() }
  val yRange = remember(constraints) { 0f..constraints.maxHeight.toFloat() }

  if (x !in xRange || y !in yRange) {
    onDisappear()
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    drawCircle(
      color = theme.backgroundStar,
      radius = particle.size,
      center = Offset(x, y),
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
  val minSpeed: Float = 0.5f,
  val maxSpeed: Float = 5f,
  val blur: StarryBlur = StarryBlur.Medium,
) {
  companion object {
    val Default = StarryBackgroundConfig()
  }
}

@Immutable
enum class StarryBlur { None, Low, Medium, High, Max, }

@Immutable
private data class Particle(
  val startX: Float,
  val startY: Float,
  val velocityX: Float,
  val velocityY: Float,
  val size: Float,
)

private enum class XDirection(val factor: Int) { Left(factor = 1), Right(factor = -1), }
private enum class YDirection(val factor: Int) { Up(factor = 1), Down(factor = -1), }

private fun generateParticle(
  constraints: Constraints,
  random: Random,
  config: StarryBackgroundConfig,
): Particle {
  val xDir = if (random.nextBoolean()) XDirection.Left else XDirection.Right
  val yDir = if (random.nextBoolean()) YDirection.Up else YDirection.Down
  val x = when (xDir) {
    XDirection.Left -> constraints.maxWidth.toFloat()
    XDirection.Right -> 0f
  }
  val y = when (yDir) {
    YDirection.Up -> constraints.maxWidth.toFloat()
    YDirection.Down -> 0f
  }
  val size = random.nextFloat() * (config.maxSize - config.minSize) + config.minSize
  val vx = (random.nextFloat() * (config.maxSpeed - config.minSpeed) + config.minSpeed) * xDir.factor
  val vy = (random.nextFloat() * (config.maxSpeed - config.minSpeed) + config.minSpeed) * yDir.factor
  return Particle(x, y, vx, vy, size)
}

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
