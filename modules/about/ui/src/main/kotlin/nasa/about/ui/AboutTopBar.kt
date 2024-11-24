package nasa.about.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import nasa.about.res.AboutStrings
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors

@Composable
internal fun AboutTopBar(
  theme: Theme,
  scrollBehavior: TopAppBarScrollBehavior,
  onAction: (AboutAction) -> Unit,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    navigationIcon = {
      IconButton(onClick = { onAction(AboutAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = AboutStrings.toolbarBack,
        )
      }
    },
    title = {
      Text(
        text = AboutStrings.toolbarTitle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    scrollBehavior = scrollBehavior,
  )
}
