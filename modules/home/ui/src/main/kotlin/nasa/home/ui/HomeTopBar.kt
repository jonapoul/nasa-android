package nasa.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors

@Composable
internal fun HomeTopBar(
  onAction: (HomeAction) -> Unit,
  theme: Theme,
  scrollBehavior: TopAppBarScrollBehavior,
) {
  TopAppBar(
    scrollBehavior = scrollBehavior,
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = stringResource(id = R.string.home_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    actions = {
      var showMenu by remember { mutableStateOf(false) }

      IconButton(onClick = { showMenu = !showMenu }) {
        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = stringResource(id = R.string.home_toolbar_menu),
        )
      }

      DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
      ) {
        val settingsText = stringResource(id = R.string.home_menu_settings)
        DropdownMenuItem(
          onClick = { onAction(HomeAction.NavSettings) },
          text = { Text(settingsText) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = settingsText) },
        )
        val aboutText = stringResource(id = R.string.home_menu_about)
        DropdownMenuItem(
          onClick = { onAction(HomeAction.NavAbout) },
          text = { Text(aboutText) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Info, contentDescription = aboutText) },
        )
      }
    },
  )
}
