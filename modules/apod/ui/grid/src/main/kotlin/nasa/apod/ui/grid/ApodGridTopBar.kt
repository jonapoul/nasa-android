package nasa.apod.ui.grid

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nasa.apod.res.R
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn
import nasa.core.res.R as CoreR

@Composable
internal fun ApodGridTopBar(
  state: GridScreenState,
  showBackButton: Boolean,
  onAction: (ApodGridAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      Text(
        text = stringResource(id = R.string.apod_grid_toolbar_title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      if (showBackButton) {
        IconButton(onClick = { onAction(ApodGridAction.NavBack) }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = CoreR.string.nav_back),
          )
        }
      }
    },
    actions = {
      IconButton(onClick = { onAction(ApodGridAction.LoadRandom) }) {
        Icon(
          imageVector = Icons.Filled.Shuffle,
          contentDescription = stringResource(id = R.string.apod_grid_toolbar_random),
        )
      }
      IconButton(onClick = { onAction(ApodGridAction.SearchMonth(state.dateOrNull())) }) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = stringResource(id = R.string.apod_grid_toolbar_search),
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewToolbar() = PreviewColumn {
  ApodGridTopBar(
    state = GridScreenState.Success(EXAMPLE_ITEMS, EXAMPLE_KEY),
    showBackButton = true,
    onAction = {},
  )
}
