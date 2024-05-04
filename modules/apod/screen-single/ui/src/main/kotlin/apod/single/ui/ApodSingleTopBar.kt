package apod.single.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import apod.core.ui.color.Theme
import apod.core.ui.color.topAppBarColors
import apod.single.res.R
import apod.single.vm.ApodSingleAction
import apod.single.vm.ScreenState

@Composable
internal fun ApodSingleTopBar(
  state: ScreenState,
  theme: Theme,
  onAction: (ApodSingleAction) -> Unit,
) {
  val key = state.apiKeyOrNull()

  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = stringResource(id = R.string.apod_single_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    actions = {
      if (key != null) {
        IconButton(onClick = { onAction(ApodSingleAction.LoadRandom(key)) }) {
          Icon(
            imageVector = Icons.Filled.Shuffle,
            contentDescription = stringResource(id = R.string.apod_single_toolbar_random),
          )
        }
      }

      var showMenu by remember { mutableStateOf(false) }

      IconButton(onClick = { showMenu = !showMenu }) {
        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = stringResource(id = R.string.apod_single_toolbar_menu),
        )
      }

      DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
      ) {
        val settingsText = stringResource(id = R.string.apod_single_menu_settings)
        DropdownMenuItem(
          onClick = { onAction(ApodSingleAction.NavSettings) },
          text = { Text(settingsText) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = settingsText) },
        )
        val aboutText = stringResource(id = R.string.apod_single_menu_about)
        DropdownMenuItem(
          onClick = { onAction(ApodSingleAction.NavAbout) },
          text = { Text(aboutText) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Info, contentDescription = aboutText) },
        )
      }
    },
  )
}
