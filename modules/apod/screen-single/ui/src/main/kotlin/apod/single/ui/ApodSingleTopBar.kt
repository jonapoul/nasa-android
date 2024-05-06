package apod.single.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.color.topAppBarColors
import apod.core.ui.preview.PreviewColumn
import apod.single.vm.ScreenState
import apod.single.vm.dateOrNull
import apod.core.ui.R as CoreR

@Composable
internal fun ApodSingleTopBar(
  state: ScreenState,
  showBackButton: Boolean,
  onAction: (ApodSingleAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  val key = state.apiKeyOrNull()
  val date = state.dateOrNull()

  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = stringResource(id = R.string.apod_single_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      if (showBackButton) {
        IconButton(onClick = { onAction(ApodSingleAction.NavBack) }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = CoreR.string.nav_back),
          )
        }
      }
    },
    actions = {
      if (date != null) {
        IconButton(onClick = { onAction(ApodSingleAction.NavGrid(date)) }) {
          Icon(
            imageVector = Icons.Filled.GridView,
            contentDescription = stringResource(id = R.string.apod_single_toolbar_grid),
          )
        }
      }

      if (key != null) {
        IconButton(onClick = { onAction(ApodSingleAction.LoadRandom) }) {
          Icon(
            imageVector = Icons.Filled.Shuffle,
            contentDescription = stringResource(id = R.string.apod_single_toolbar_random),
          )
        }
      }

      IconButton(onClick = { onAction(ApodSingleAction.SearchDate(state.dateOrNull())) }) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = stringResource(id = R.string.apod_single_toolbar_search),
        )
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

@Preview
@Composable
private fun PreviewSuccess() = PreviewColumn {
  ApodSingleTopBar(
    state = ScreenState.Success(EXAMPLE_ITEM, EXAMPLE_KEY),
    showBackButton = true,
    onAction = {},
  )
}
