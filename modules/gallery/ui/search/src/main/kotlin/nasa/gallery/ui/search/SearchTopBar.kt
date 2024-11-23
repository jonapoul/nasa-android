package nasa.gallery.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nasa.core.res.CoreStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.res.GalleryStrings

@Composable
internal fun SearchTopBar(
  onAction: (SearchAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = GalleryStrings.searchToolbarTitle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      IconButton(onClick = { onAction(SearchAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = CoreStrings.navBack,
        )
      }
    },
    actions = {
      IconButton(onClick = { onAction(SearchAction.ShowViewConfigDialog) }) {
        Icon(
          contentDescription = GalleryStrings.searchToolbarViewType,
          imageVector = Icons.Filled.Settings,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewToolbar() = PreviewColumn {
  SearchTopBar(
    onAction = {},
  )
}
