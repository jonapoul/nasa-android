@file:Suppress("UnusedPrivateMember")

package nasa.gallery.search.ui

import alakazam.android.ui.compose.HorizontalSpacer
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.button.RegularTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.slider
import nasa.core.ui.preview.MY_PHONE_WIDTH_DP
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.text.NasaTextField
import nasa.gallery.model.Center
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.Photographer
import nasa.gallery.model.Year
import nasa.gallery.res.R
import kotlin.math.roundToInt

@Composable
internal fun SearchFilterModal(
  config: FilterConfig,
  onConfirm: (FilterConfig) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  ModalBottomSheet(
    modifier = modifier.fillMaxWidth(),
    onDismissRequest = onClose,
  ) {
    SearchFilterModalContents(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .navigationBarsPadding(),
      config = config,
      onConfirm = onConfirm,
      onCancel = onClose,
      theme = theme,
    )
  }
}

@Composable
private fun SearchFilterModalContents(
  config: FilterConfig,
  onConfirm: (FilterConfig) -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val mutableConfig = remember(config) { MutableFilterConfig(config) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    YearRange(
      start = mutableConfig.yearStart,
      end = mutableConfig.yearEnd,
      theme = theme,
      onStartChanged = { mutableConfig.yearStart = it },
      onEndChanged = { mutableConfig.yearEnd = it },
    )

    ModalTextField(
      value = mutableConfig.title,
      label = R.string.search_label_title,
      onValueChanged = { mutableConfig.title = it },
      theme = theme,
    )

    ModalTextField(
      value = mutableConfig.center,
      label = R.string.search_label_center,
      onValueChanged = { mutableConfig.center = it?.let(::Center) },
      theme = theme,
    )

    ModalTextField(
      value = mutableConfig.location,
      label = R.string.search_label_location,
      onValueChanged = { mutableConfig.location = it },
      theme = theme,
    )

    ModalTextField(
      value = mutableConfig.photographer,
      label = R.string.search_label_photographer,
      onValueChanged = { mutableConfig.photographer = it?.let(::Photographer) },
      theme = theme,
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = FIELD_PADDING),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      PrimaryTextButton(
        text = stringResource(id = R.string.search_input_apply),
        modifier = Modifier.weight(1f),
        theme = theme,
        onClick = { onConfirm(mutableConfig.toFilterConfig()) },
      )

      HorizontalSpacer(8.dp)

      RegularTextButton(
        text = stringResource(id = R.string.search_input_cancel),
        modifier = Modifier.weight(1f),
        theme = theme,
        onClick = onCancel,
      )
    }
  }
}

@Composable
private fun YearRange(
  start: Year,
  end: Year,
  onStartChanged: (Year) -> Unit,
  onEndChanged: (Year) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val currentRange = remember(start, end) { start.toFloat()..end.toFloat() }
  val allowedRange = remember { Year.Minimum.toFloat()..Year.Maximum.toFloat() }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(R.string.search_label_date, start.value, end.value),
      textAlign = TextAlign.Start,
      fontSize = 14.sp,
      color = theme.pageText,
    )

    RangeSlider(
      modifier = modifier
        .height(30.dp)
        .padding(horizontal = 4.dp),
      value = currentRange,
      valueRange = allowedRange,
      onValueChange = { newRange ->
        onStartChanged(newRange.start.roundToInt().let(::Year))
        onEndChanged(newRange.endInclusive.roundToInt().let(::Year))
      },
      colors = theme.slider(),
    )
  }
}

@Composable
private fun ModalTextField(
  value: CharSequence?,
  @StringRes label: Int,
  onValueChanged: (String?) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  NasaTextField(
    modifier = modifier
      .padding(FIELD_PADDING)
      .fillMaxWidth(),
    value = value?.toString().orEmpty(),
    onValueChange = { onValueChanged(it.ifEmpty { null }) },
    placeholderText = stringResource(id = R.string.search_input_placeholder),
    label = { Text(stringResource(label)) },
    clearable = true,
    theme = theme,
  )
}

private fun MutableFilterConfig.toFilterConfig() = FilterConfig(
  query = query,
  center = center,
  description = description,
  keywords = keywords,
  location = location,
  mediaTypes = mediaTypes,
  nasaId = nasaId,
  photographer = photographer,
  secondaryCreator = secondaryCreator,
  title = title,
  yearStart = yearStart,
  yearEnd = yearEnd,
)

private val FIELD_PADDING = 4.dp

@Preview(
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED,
  widthDp = 3 * MY_PHONE_WIDTH_DP,
)
internal annotation class ModalPreview

@ModalPreview
@Composable
private fun PreviewEmpty() = PreviewModal(FilterConfig.Empty)

@ModalPreview
@Composable
@Suppress("MagicNumber")
private fun PreviewFilled() = PreviewModal(
  FilterConfig(
    title = "Searching for title with very long string like this to see how it behaves over multiple lines",
    center = Center("ABC"),
    location = "Somewhere",
    photographer = Photographer("Dick Dastardly"),
    yearStart = Year(1950),
    yearEnd = Year(1972),
  ),
)

@Composable
private fun PreviewModal(config: FilterConfig) = PreviewScreen(
  previewModifier = Modifier.width(MY_PHONE_WIDTH_DP.dp),
) {
  SearchFilterModalContents(
    config = config,
    onConfirm = {},
    onCancel = {},
  )
}
