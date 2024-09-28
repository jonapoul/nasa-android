package nasa.gallery.ui.search

import alakazam.android.ui.compose.HorizontalSpacer
import alakazam.android.ui.compose.VerticalSpacer
import alakazam.kotlin.core.fastIsBlank
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.CardShape
import nasa.core.ui.button.PrimaryIconButton
import nasa.core.ui.button.RegularIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.slider
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.text.NasaTextField
import nasa.core.ui.text.keyboardFocusRequester
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import nasa.gallery.res.R
import kotlin.math.roundToInt

@Composable
internal fun SearchInput(
  filterConfig: FilterConfig,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  searchInput: String = "",
  theme: Theme = LocalTheme.current,
) {
  var showExtraConfig by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    TopRow(
      modifier = Modifier.fillMaxWidth(),
      onAction = onAction,
      onExpandExtraConfig = { showExtraConfig = !showExtraConfig },
      searchInput = searchInput,
      theme = theme,
    )

    AnimatedVisibility(
      visible = showExtraConfig,
      enter = slideInVertically(),
      exit = slideOutVertically(),
    ) {
      ExtraConfig(
        config = filterConfig,
        theme = theme,
        onAction = onAction,
      )
    }
  }
}

@Composable
private fun TopRow(
  onAction: (SearchAction) -> Unit,
  onExpandExtraConfig: () -> Unit,
  modifier: Modifier = Modifier,
  searchInput: String = "",
  theme: Theme = LocalTheme.current,
) {
  var text by remember { mutableStateOf(searchInput) }
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val keyboard = LocalSoftwareKeyboardController.current

    NasaTextField(
      modifier = Modifier
        .weight(1f)
        .focusRequester(keyboardFocusRequester(keyboard)),
      value = text,
      onValueChange = {
        text = it
        onAction(SearchAction.EnterSearchTerm(it))
      },
      placeholderText = stringResource(id = R.string.search_input_hint),
      theme = theme,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(
        onSearch = {
          onAction(SearchAction.PerformSearch)
          keyboard?.hide()
        },
      ),
      clearable = true,
    )

    HorizontalSpacer(8.dp)

    PrimaryIconButton(
      imageVector = Icons.Filled.Search,
      contentDescription = stringResource(id = R.string.search_input_submit),
      onClick = {
        onAction(SearchAction.PerformSearch)
        keyboard?.hide()
      },
      enabled = !text.fastIsBlank(),
      theme = theme,
    )

    HorizontalSpacer(8.dp)

    RegularIconButton(
      imageVector = Icons.Filled.Tune,
      contentDescription = stringResource(id = R.string.search_input_settings),
      onClick = {
        onExpandExtraConfig()
        keyboard?.hide()
      },
      enabled = true,
      theme = theme,
    )
  }
}

@Composable
private fun ExtraConfig(
  config: FilterConfig,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
  ) {
    VerticalSpacer(4.dp)

    MediaTypesRow(
      modifier = Modifier.fillMaxWidth(),
      onAction = onAction,
      mediaTypes = config.mediaTypes ?: MediaTypes.All,
      theme = theme,
    )

    VerticalSpacer(4.dp)

    YearRange(
      modifier = Modifier,
      config = config,
      theme = theme,
      onAction = onAction,
    )
  }
}

@Composable
private fun MediaTypesRow(
  mediaTypes: MediaTypes,
  onAction: (SearchAction) -> Unit,
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(theme.cardBackground, CardShape),
    horizontalArrangement = Arrangement.Center,
  ) {
    val types = remember(mediaTypes) { mediaTypes.toSet() }

    MediaTypeCheckbox(
      modifier = Modifier.weight(1f),
      text = stringResource(R.string.search_label_image),
      isChecked = MediaType.Image in types,
      onCheckedChange = { checked -> submitTypesOnChange(checked, types, MediaType.Image, onAction) },
      theme = theme,
    )
    MediaTypeCheckbox(
      modifier = Modifier.weight(1f),
      text = stringResource(R.string.search_label_video),
      isChecked = MediaType.Video in types,
      onCheckedChange = { checked -> submitTypesOnChange(checked, types, MediaType.Video, onAction) },
      theme = theme,
    )
    MediaTypeCheckbox(
      modifier = Modifier.weight(1f),
      text = stringResource(R.string.search_label_audio),
      isChecked = MediaType.Audio in types,
      onCheckedChange = { checked -> submitTypesOnChange(checked, types, MediaType.Audio, onAction) },
      theme = theme,
    )
  }
}

@Composable
private fun MediaTypeCheckbox(
  text: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  val mutableInteractionSource = remember { MutableInteractionSource() }

  var localIsChecked by remember { mutableStateOf(isChecked) }

  Row(
    modifier = modifier.clickable(mutableInteractionSource, ripple()) {
      localIsChecked = !isChecked
      onCheckedChange(!isChecked)
    },
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Checkbox(
      modifier = Modifier.wrapContentSize(),
      checked = localIsChecked,
      onCheckedChange = onCheckedChange,
      colors = theme.mediaTypeCheckbox(),
      interactionSource = mutableInteractionSource,
    )

    Text(
      text = text,
      fontSize = 14.sp,
      color = theme.buttonRegularText,
    )
  }
}

@Stable
@Composable
private fun Theme.mediaTypeCheckbox() = CheckboxDefaults.colors(
  checkedColor = checkboxChecked,
  uncheckedColor = buttonRegularText,
  checkmarkColor = checkboxCheckmark,
)

private fun submitTypesOnChange(
  checked: Boolean,
  mediaTypes: Set<MediaType>,
  type: MediaType,
  onAction: (SearchAction) -> Unit,
) {
  val newTypes = if (checked) mediaTypes + type else mediaTypes - type
  onAction(SearchAction.SetMediaTypes(MediaTypes(newTypes)))
}

@Composable
private fun YearRange(
  config: FilterConfig,
  theme: Theme,
  modifier: Modifier = Modifier,
  onAction: (SearchAction) -> Unit,
) {
  val start = remember(config) { config.yearStart ?: Year.Minimum }
  val end = remember(config) { config.yearEnd ?: Year.Maximum }
  val currentRange = remember(start, end) { start.toFloat()..end.toFloat() }
  val allowedRange = remember { Year.Minimum.toFloat()..Year.Maximum.toFloat() }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(theme.cardBackground, CardShape)
      .padding(vertical = 4.dp, horizontal = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(R.string.search_label_year, start.value, end.value),
      textAlign = TextAlign.Start,
      fontSize = 14.sp,
      color = theme.pageText,
    )

    RangeSlider(
      modifier = Modifier
        .height(30.dp)
        .padding(horizontal = 4.dp),
      value = currentRange,
      valueRange = allowedRange,
      onValueChange = { newRange ->
        val newStart = newRange.start.roundToInt().let(::Year)
        val newEnd = newRange.endInclusive.roundToInt().let(::Year)
        onAction(SearchAction.SetYearRange(newStart, newEnd))
      },
      colors = theme.slider(),
    )
  }
}

@Preview
@Composable
private fun PreviewSearchInput() = PreviewColumn {
  SearchInput(
    searchInput = "abc123",
    filterConfig = PREVIEW_FILTER_CONFIG,
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewEmpty() = PreviewColumn {
  SearchInput(
    searchInput = "",
    filterConfig = PREVIEW_FILTER_CONFIG.copy(
      mediaTypes = MediaTypes(emptySet()),
    ),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewExtraConfigDefault() = PreviewColumn {
  ExtraConfig(
    config = PREVIEW_FILTER_CONFIG,
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewExtraConfigCustomYears() = PreviewColumn {
  ExtraConfig(
    config = PREVIEW_FILTER_CONFIG.copy(
      yearStart = Year(1989),
      yearEnd = Year(2007),
    ),
    onAction = {},
  )
}
