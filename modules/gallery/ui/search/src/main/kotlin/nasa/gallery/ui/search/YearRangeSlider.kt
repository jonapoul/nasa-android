package nasa.gallery.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.NasaRangeSliderTrack
import nasa.core.ui.NasaSliderThumb
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.slider
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.model.Year
import nasa.gallery.model.year
import kotlin.math.roundToInt

@Composable
internal fun YearRangeSlider(
  start: Year?,
  end: Year?,
  onNewRange: (start: Year, end: Year) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val startYear = remember(start) { start ?: Year.Minimum }
  val endYear = remember(end) { end ?: Year.Maximum }
  val currentRange = remember(startYear, endYear) { startYear.toFloat()..endYear.toFloat() }
  val colors = theme.slider()

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
  ) {
    RangeSlider(
      modifier = Modifier.height(30.dp),
      value = currentRange,
      valueRange = ALLOWED_RANGE,
      colors = colors,
      startThumb = { NasaSliderThumb(colors) },
      endThumb = { NasaSliderThumb(colors) },
      track = { state -> NasaRangeSliderTrack(state, colors) },
      onValueChange = { range ->
        val newStart = range.start.roundToInt().year
        val newEnd = range.endInclusive.roundToInt().year
        onNewRange(newStart, newEnd)
      },
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text(
        modifier = Modifier.weight(1f),
        text = startYear.toString(),
        textAlign = TextAlign.Start,
        fontSize = 13.sp,
      )
      Text(
        modifier = Modifier.weight(1f),
        text = endYear.toString(),
        textAlign = TextAlign.End,
        fontSize = 13.sp,
      )
    }
  }
}

private val ALLOWED_RANGE = Year.Minimum.toFloat()..Year.Maximum.toFloat()

@Preview
@Composable
private fun PreviewFullRange() = PreviewColumn {
  YearRangeSlider(
    start = Year.Minimum,
    end = Year.Maximum,
    onNewRange = { _, _ -> },
    modifier = Modifier.padding(10.dp),
  )
}

@Preview
@Composable
private fun PreviewPartialRange() = PreviewColumn {
  YearRangeSlider(
    start = 1950.year,
    end = 1993.year,
    onNewRange = { _, _ -> },
    modifier = Modifier.padding(10.dp),
  )
}

@Preview
@Composable
private fun PreviewTinyRange() = PreviewColumn {
  YearRangeSlider(
    start = 1960.year,
    end = 1961.year,
    onNewRange = { _, _ -> },
    modifier = Modifier.padding(10.dp),
  )
}
