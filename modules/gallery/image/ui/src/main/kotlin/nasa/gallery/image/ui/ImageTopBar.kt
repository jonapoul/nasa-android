package nasa.gallery.image.ui

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
import nasa.core.res.R as CoreR

@Composable
internal fun ImageTopBar(
  title: String,
  onAction: (ImageAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      IconButton(onClick = { onAction(ImageAction.NavBack) }) {
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
  ImageTopBar(
    title = "Hello world",
    onAction = {},
  )
}
