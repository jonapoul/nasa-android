package nasa.gallery.search.ui

import alakazam.android.ui.compose.HorizontalSpacer
import alakazam.kotlin.core.fastIsBlank
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.button.PrimaryIconButton
import nasa.core.ui.button.RegularIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.text.NasaTextField
import nasa.core.ui.text.keyboardFocusRequester
import nasa.gallery.res.R

@Composable
internal fun SearchInput(
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  searchInput: String = "",
  theme: Theme = LocalTheme.current,
) {
  var text by remember { mutableStateOf(searchInput) }
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp),
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
        onAction(SearchAction.ConfigureSearch)
        keyboard?.hide()
      },
      enabled = true,
      theme = theme,
    )
  }
}

@Preview
@Composable
private fun PreviewSearchInput() = PreviewColumn {
  SearchInput(
    searchInput = "abc123",
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewEmpty() = PreviewColumn {
  SearchInput(
    searchInput = "",
    onAction = {},
  )
}
