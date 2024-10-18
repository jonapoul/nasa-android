package nasa.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NasaSliderThumb(
  colors: SliderColors,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .size(THUMB_SIZE)
      .background(colors.thumbColor, THUMB_SHAPE),
  )
}

private val THUMB_SIZE = 20.dp
private val THUMB_SHAPE = RoundedCornerShape(size = 2.dp)

@Composable
fun NasaRangeSliderTrack(
  state: RangeSliderState,
  colors: SliderColors,
  modifier: Modifier = Modifier,
  trackHeight: Dp = 8.dp,
) {
  val fractionStart by remember {
    derivedStateOf {
      (state.activeRangeStart - state.valueRange.start) / (state.valueRange.endInclusive - state.valueRange.start)
    }
  }
  val fractionEnd by remember {
    derivedStateOf {
      (state.activeRangeEnd - state.valueRange.start) / (state.valueRange.endInclusive - state.valueRange.start)
    }
  }

  val startWeight = fractionStart
  val midWeight = fractionEnd - fractionStart
  val endWeight = 1f - fractionEnd

  Row(
    modifier = modifier,
  ) {
    if (startWeight != 0f) {
      Box(
        modifier = Modifier
          .weight(startWeight)
          .height(trackHeight)
          .background(colors.inactiveTrackColor, CircleShape),
      )
    }
    if (midWeight != 0f) {
      Box(
        modifier = Modifier
          .weight(midWeight)
          .height(trackHeight)
          .background(colors.activeTrackColor, CircleShape),
      )
    }
    if (endWeight != 0f) {
      Box(
        modifier = Modifier
          .weight(endWeight)
          .height(trackHeight)
          .background(colors.inactiveTrackColor, CircleShape),
      )
    }
  }
}

@Composable
fun NasaSliderTrack(
  state: SliderState,
  colors: SliderColors,
  modifier: Modifier = Modifier,
  trackHeight: Dp = 8.dp,
) {
  val fraction by remember {
    derivedStateOf {
      (state.value - state.valueRange.start) / (state.valueRange.endInclusive - state.valueRange.start)
    }
  }

  val startWeight = fraction
  val endWeight = 1f - fraction

  Row(
    modifier = modifier,
  ) {
    if (startWeight != 0f) {
      Box(
        modifier = Modifier
          .weight(startWeight)
          .height(trackHeight)
          .background(colors.activeTrackColor, CircleShape),
      )
    }
    if (endWeight != 0f) {
      Box(
        modifier = Modifier
          .weight(endWeight)
          .height(trackHeight)
          .background(colors.inactiveTrackColor, CircleShape),
      )
    }
  }
}
