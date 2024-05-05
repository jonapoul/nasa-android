package apod.settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import apod.core.ui.color.Theme
import apod.core.ui.color.topAppBarColors
import apod.settings.vm.SettingsAction

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
          contentDescription = stringResource(id = R.string.settings_toolbar_back),
        )
      }
    },
    title = {
      Text(
        text = stringResource(id = R.string.settings_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    scrollBehavior = scrollBehavior,
  )
}
