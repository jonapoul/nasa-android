package nasa.settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.settings.res.SettingsStrings

@Composable
internal fun SettingsTopBar(
  theme: Theme,
  scrollBehavior: TopAppBarScrollBehavior,
  onAction: (SettingsAction) -> Unit,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    navigationIcon = {
      IconButton(onClick = { onAction(SettingsAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = SettingsStrings.toolbarBack,
        )
      }
    },
    title = {
      Text(
        text = SettingsStrings.toolbarTitle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    scrollBehavior = scrollBehavior,
  )
}
