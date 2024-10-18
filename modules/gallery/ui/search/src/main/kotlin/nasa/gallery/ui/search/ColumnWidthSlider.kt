package nasa.gallery.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.NasaSliderThumb
import nasa.core.ui.NasaSliderTrack
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.slider
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.res.R
import kotlin.math.roundToInt

@Composable
internal fun ColumnWidthSlider(
  value: Int,
  onNewValue: (Int) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val colors = theme.slider()

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text(
        modifier = Modifier.wrapContentWidth(),
        text = stringResource(R.string.search_config_width),
        textAlign = TextAlign.Start,
        fontSize = 13.sp,
      )

      Text(
        modifier = Modifier.weight(1f),
        text = value.toString(),
        textAlign = TextAlign.End,
        fontSize = 13.sp,
      )
    }

    Slider(
      modifier = Modifier.height(30.dp),
      value = value.toFloat(),
      valueRange = ALLOWED_RANGE,
      colors = colors,
      thumb = { NasaSliderThumb(colors) },
      track = { state -> NasaSliderTrack(state, colors) },
      onValueChange = { onNewValue(it.roundToInt()) },
    )
  }
}

private val ALLOWED_RANGE = 40f..200f

@Preview
@Composable
private fun PreviewFullRange() = PreviewColumn {
  ColumnWidthSlider(
    value = 200,
    onNewValue = {},
    modifier = Modifier.padding(10.dp),
  )
}

@Preview
@Composable
private fun PreviewPartialRange() = PreviewColumn {
  ColumnWidthSlider(
    value = 120,
    onNewValue = {},
    modifier = Modifier.padding(10.dp),
  )
}

@Preview
@Composable
private fun PreviewTinyRange() = PreviewColumn {
  ColumnWidthSlider(
    value = 40,
    onNewValue = {},
    modifier = Modifier.padding(10.dp),
  )
}
