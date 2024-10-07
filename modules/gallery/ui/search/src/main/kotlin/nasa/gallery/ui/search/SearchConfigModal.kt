package nasa.gallery.ui.search

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.button.RegularTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import nasa.gallery.model.year
import nasa.gallery.res.R

@Composable
internal fun SearchConfigModal(
  config: FilterConfig,
  onAction: (SearchAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  ModalBottomSheet(
    onDismissRequest = { onAction(SearchAction.ToggleExtraConfig) },
  ) {
    SearchConfigModalContents(
      config = config,
      theme = theme,
      onConfirm = { newConfig ->
        onAction(SearchAction.ToggleExtraConfig)
        onAction(SearchAction.SetFilterConfig(newConfig))
        onAction(SearchAction.PerformSearch)
      },
      onReset = { onAction(SearchAction.ResetExtraConfig) },
      onCancel = { onAction(SearchAction.ToggleExtraConfig) },
    )
  }
}

@Composable
private fun SearchConfigModalContents(
  config: FilterConfig,
  onConfirm: (FilterConfig) -> Unit,
  onReset: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  var mediaTypes by remember(config) { mutableStateOf(config.mediaTypes ?: MediaTypes.All) }
  var startYear by remember(config) { mutableStateOf(config.startYear) }
  var endYear by remember(config) { mutableStateOf(config.endYear) }

  Column(
    modifier = modifier.fillMaxWidth(),
  ) {
    MediaTypesRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      onChange = { types -> mediaTypes = types },
      mediaTypes = mediaTypes,
      theme = theme,
    )

    HorizontalDivider(
      modifier = Modifier.padding(vertical = 4.dp),
    )

    YearRangeSlider(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
      start = startYear,
      end = endYear,
      theme = theme,
      onNewRange = { start, end ->
        startYear = start
        endYear = end
      },
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
    ) {
      PrimaryTextButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_confirm),
        theme = theme,
        onClick = {
          val newConfig = config.copy(yearStart = startYear, yearEnd = endYear, mediaTypes = mediaTypes)
          onConfirm(newConfig)
        },
      )

      HorizontalSpacer(4.dp)

      RegularTextButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_reset),
        theme = theme,
        onClick = onReset,
      )

      HorizontalSpacer(4.dp)

      RegularTextButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_dismiss),
        theme = theme,
        onClick = onCancel,
      )
    }
  }
}

@Preview
@Composable
private fun PreviewStandard() = PreviewColumn {
  SearchConfigModalContents(
    config = PREVIEW_FILTER_CONFIG,
    onConfirm = {},
    onReset = {},
    onCancel = {},
  )
}

@Preview
@Composable
private fun PreviewAlt() = PreviewColumn {
  SearchConfigModalContents(
    config = PREVIEW_FILTER_CONFIG.copy(
      mediaTypes = MediaTypes(MediaType.Video),
      yearStart = 1950.year,
      yearEnd = 1980.year,
    ),
    onConfirm = {},
    onReset = {},
    onCancel = {},
  )
}
