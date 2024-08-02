package nasa.gallery.search.ui

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.button.RegularTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.textField
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.core.ui.text.NasaTextField
import nasa.gallery.search.vm.DefaultFilterConfig
import nasa.gallery.search.vm.FilterConfig

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
    containerColor = theme.cardBackground,
  ) {
    SearchFilterModalContents(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
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
  var center by remember { mutableStateOf(config.center.orEmpty()) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(theme.cardBackground),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    ModalTextField(
      value = center,
      label = stringResource(id = R.string.search_label_center),
      onValueChanged = { center = it },
      theme = theme,
    )

    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
//      items() {
//
//      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      PrimaryTextButton(
        text = stringResource(id = R.string.search_input_apply),
        modifier = Modifier.weight(1f),
        onClick = {
          val newConfig = FilterConfig(config.query, center)
          onConfirm(newConfig)
        },
      )

      HorizontalSpacer(20.dp)

      RegularTextButton(
        text = stringResource(id = R.string.search_input_cancel),
        modifier = Modifier.weight(1f),
        onClick = onCancel,
      )
    }
  }
}

@Composable
private fun ModalTextField(
  value: String,
  label: String,
  onValueChanged: (String) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  NasaTextField(
    modifier = modifier,
    value = value,
    onValueChange = onValueChanged,
    placeholderText = stringResource(id = R.string.search_input_placeholder),
    label = { Text(label) },
    colors = theme.textField(focusedContainer = Color.Transparent),
  )
}

@Preview
@Composable
private fun PreviewTextFieldFilled() = PreviewColumn {
  ModalTextField(
    value = "Hello world",
    label = "Search Field",
    onValueChanged = {},
  )
}

@Preview
@Composable
private fun PreviewTextFieldEmpty() = PreviewColumn {
  ModalTextField(
    value = "",
    label = "Search Field",
    onValueChanged = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewContents() = PreviewScreen {
  SearchFilterModalContents(
    config = DefaultFilterConfig,
    onConfirm = {},
    onCancel = {},
  )
}
