package nasa.gallery.search.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.search.vm.SearchScreenState
import nasa.core.ui.R as CoreR

@Suppress("UnusedParameter")
@Composable
internal fun SearchTopBar(
  state: SearchScreenState,
  onAction: (SearchAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = stringResource(id = R.string.gallery_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      IconButton(onClick = { onAction(SearchAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(id = CoreR.string.nav_back),
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewToolbar() = PreviewColumn {
  SearchTopBar(
    state = SearchScreenState.Success(""),
    onAction = {},
  )
}
