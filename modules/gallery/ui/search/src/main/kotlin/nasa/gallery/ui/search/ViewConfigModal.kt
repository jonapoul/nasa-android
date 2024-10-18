package nasa.gallery.ui.search

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.button.RegularTextButton
import nasa.core.ui.button.ToggleableButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.model.SearchViewConfig
import nasa.gallery.model.SearchViewType
import nasa.gallery.res.R

@Composable
internal fun ViewConfigModal(
  config: SearchViewConfig,
  onAction: (SearchAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  ModalBottomSheet(
    onDismissRequest = { onAction(SearchAction.HideViewTypeDialog) },
  ) {
    ViewTypeModalContents(
      config = config,
      theme = theme,
      onConfirm = { config ->
        onAction(SearchAction.SetViewConfig(config))
        onAction(SearchAction.HideViewTypeDialog)
      },
      onReset = {
        onAction(SearchAction.ResetViewConfig)
        onAction(SearchAction.HideViewTypeDialog)
      },
      onDismiss = { onAction(SearchAction.HideViewTypeDialog) },
    )
  }
}

@Composable
private fun ViewTypeModalContents(
  config: SearchViewConfig,
  onConfirm: (SearchViewConfig) -> Unit,
  onReset: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  var mutableViewType by remember(config) { mutableStateOf(config.type) }
  var mutableColumnWidth by remember(config) { mutableIntStateOf(config.columnWidthDp) }

  Column(
    modifier = modifier.fillMaxWidth(),
  ) {
    if (mutableViewType == SearchViewType.Grid) {
      ColumnWidthSlider(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        value = mutableColumnWidth,
        theme = theme,
        onNewValue = { mutableColumnWidth = it },
      )

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
      )
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.Center,
    ) {
      ViewTypeButton(
        modifier = Modifier.weight(1f),
        type = SearchViewType.Grid,
        selectedType = mutableViewType,
        theme = theme,
        onSelect = { mutableViewType = it },
      )

      ViewTypeButton(
        modifier = Modifier.weight(1f),
        type = SearchViewType.Card,
        selectedType = mutableViewType,
        theme = theme,
        onSelect = { mutableViewType = it },
      )
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
    ) {
      PrimaryTextButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.search_modal_confirm),
        theme = theme,
        onClick = { onConfirm(SearchViewConfig(mutableViewType, mutableColumnWidth)) },
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
        onClick = onDismiss,
      )
    }
  }
}

@Composable
private fun ViewTypeButton(
  type: SearchViewType,
  selectedType: SearchViewType,
  theme: Theme,
  onSelect: (SearchViewType) -> Unit,
  modifier: Modifier = Modifier,
) {
  ToggleableButton(
    text = type.string(),
    isChecked = selectedType == type,
    icon = type.icon(),
    onCheckedChange = { onSelect(type) },
    theme = theme,
    modifier = modifier.padding(4.dp),
  )
}

@Stable
@Composable
private fun SearchViewType.string() = when (this) {
  SearchViewType.Card -> stringResource(R.string.search_view_type_card)
  SearchViewType.Grid -> stringResource(R.string.search_view_type_grid)
}

@Stable
@Composable
private fun SearchViewType.icon() = when (this) {
  SearchViewType.Card -> Icons.AutoMirrored.Filled.ViewList
  SearchViewType.Grid -> Icons.Filled.GridOn
}

@Preview
@Composable
private fun PreviewGrid() = PreviewColumn {
  ViewTypeModalContents(
    config = PREVIEW_GRID_CONFIG,
    onConfirm = {},
    onReset = {},
    onDismiss = {},
  )
}

@Preview
@Composable
private fun PreviewCard() = PreviewColumn {
  ViewTypeModalContents(
    config = PREVIEW_CARD_CONFIG,
    onConfirm = {},
    onReset = {},
    onDismiss = {},
  )
}
