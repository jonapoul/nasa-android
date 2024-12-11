package nasa.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import dev.chrisbanes.haze.HazeChildScope
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun StarryBackground(
  modifier: Modifier = Modifier,
  config: StarryBackgroundConfig = StarryBackgroundConfig(),
  theme: Theme = LocalTheme.current,
  seed: Long = remember { System.currentTimeMillis() },
) {
  BoxWithConstraints(modifier = modifier) {
    val hazeState = remember { HazeState() }
    val random = remember(seed) { Random(seed) }
    val particles = remember { mutableStateListOf<Particle>() }
    val particlesToRegenerate = remember { mutableStateListOf<Int>() }
    val constraints = constraints

    DisposableEffect(config) {
      val starCount = (constraints.maxWidth * constraints.maxHeight * config.density).roundToInt()
      repeat(starCount) { particles.add(generateParticle(InitialCoordinateFactory, constraints, random, config)) }
      onDispose { particles.clear() }
    }

    Box(
      modifier = Modifier
        .haze(hazeState)
        .fillMaxSize(),
    ) {
      val particleOffsets = particles.fastMap { p -> p to generateAnimatedOffset(p, constraints) }

      Canvas(
        modifier = Modifier.fillMaxSize(),
      ) {
        particleOffsets.fastForEach { (particle, offset) ->
          drawCircle(
            color = theme.backgroundStar,
            radius = particle.size,
            center = offset,
          )
        }
      }

      particleOffsets.fastForEachIndexed { index, (_, offset) ->
        if (!isInRange(offset, constraints)) {
          particlesToRegenerate.add(index)
        }
      }

      if (particlesToRegenerate.isNotEmpty()) {
        particlesToRegenerate.fastForEach { index ->
          particles[index] = generateParticle(LaterCoordinateFactory, constraints, random, config)
        }
        particlesToRegenerate.clear()
      }
    }

    Box(
      modifier = Modifier
        .hazeChild(hazeState) { setStyle(theme, config.blur) }
        .fillMaxSize(),
    )
  }
}

private val Constraints.xRange: ClosedFloatingPointRange<Float>
  @Stable get() = 0f..maxWidth.toFloat()

private val Constraints.yRange: ClosedFloatingPointRange<Float>
  @Stable get() = 0f..maxHeight.toFloat()

@Stable
@Composable
private fun isInRange(offset: Offset, constraints: Constraints): Boolean {
  return offset.x in constraints.xRange && offset.y in constraints.yRange
}

@Composable
private fun generateAnimatedOffset(particle: Particle, constraints: Constraints): Offset {
  val expectedDuration = calculateTweenDuration(particle, constraints)
  val infiniteTransition = rememberInfiniteTransition(label = "infinite")
  val animationSpec = infiniteRepeatable<Float>(
    animation = tween(durationMillis = expectedDuration.inWholeMilliseconds.toInt(), easing = LinearEasing),
    repeatMode = RepeatMode.Restart,
  )

  val x by infiniteTransition.animateFloat(
    label = "x",
    initialValue = particle.startX,
    targetValue = particle.startX + particle.velocityX * 300f, // Assume 300 frames of movement
    animationSpec = animationSpec,
  )

  val y by infiniteTransition.animateFloat(
    label = "y",
    initialValue = particle.startY,
    targetValue = particle.startY + particle.velocityY * 300f,
    animationSpec = animationSpec,
  )

  return Offset(x, y)
}

@Stable
private fun Int.orOne(): Int = if (this == 0) 1 else this

@Stable
private fun calculateTweenDuration(particle: Particle, constraints: Constraints): Duration {
  val dtX = particle.velocityX / constraints.maxWidth.orOne()
  val dtY = particle.velocityY / constraints.maxHeight.orOne()
  return minOf(dtX, dtY).roundToLong().seconds
}

/**
 * [density] is in units of "stars per square [dp]".
 */
@Immutable
data class StarryBackgroundConfig(
  val density: Float = 1e-4f,
  val minSize: Float = 0.5f,
  val maxSize: Float = 10f,
  val minSpeed: Float = 0.01f,
  val maxSpeed: Float = 0.2f,
  val blur: StarryBlur = StarryBlur.Medium,
)

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

private enum class StartingAxis { Top, Right, Bottom, Left, }

private data class Coordinates(val x: Float, val y: Float)

private fun interface CoordinateFactory {
  fun generate(random: Random, constraints: Constraints): Coordinates
}

private object InitialCoordinateFactory : CoordinateFactory {
  override fun generate(random: Random, constraints: Constraints) = Coordinates(
    x = random.nextFloat() * constraints.maxWidth,
    y = random.nextFloat() * constraints.maxHeight,
  )
}

private object LaterCoordinateFactory : CoordinateFactory {
  override fun generate(random: Random, constraints: Constraints): Coordinates {
    val axis = StartingAxis.entries.random(random)
    var x = 0f
    var y = 0f
    when (axis) {
      StartingAxis.Top -> {
        x = random.nextFloat() * constraints.maxWidth
      }

      StartingAxis.Right -> {
        x = constraints.maxWidth.toFloat()
        y = random.nextFloat() * constraints.maxHeight
      }

      StartingAxis.Bottom -> {
        x = random.nextFloat() * constraints.maxWidth
        y = constraints.maxHeight.toFloat()
      }

      StartingAxis.Left -> {
        y = random.nextFloat() * constraints.maxHeight
      }
    }
    return Coordinates(x, y)
  }
}

private fun generateParticle(
  coordinateFactory: CoordinateFactory,
  constraints: Constraints,
  random: Random,
  config: StarryBackgroundConfig,
): Particle {
  val coordinates = coordinateFactory.generate(random, constraints)
  val xDir = if (random.nextBoolean()) XDirection.Left else XDirection.Right
  val yDir = if (random.nextBoolean()) YDirection.Up else YDirection.Down
  val size = random.nextFloat() * (config.maxSize - config.minSize) + config.minSize
  val vx = (random.nextFloat() * (config.maxSpeed - config.minSpeed) + config.minSpeed) * xDir.factor
  val vy = (random.nextFloat() * (config.maxSpeed - config.minSpeed) + config.minSpeed) * yDir.factor
  return Particle(coordinates.x, coordinates.y, vx, vy, size)
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
      density = 1e-4f,
      minSize = 0.5f,
      maxSize = 10f,
      minSpeed = 0.01f,
      maxSpeed = 0.2f,
      blur = blur,
    ),
  )
}
