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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import nasa.apod.preview.PREVIEW_ITEM_1
import nasa.apod.preview.PREVIEW_ITEM_2
import nasa.apod.res.ApodStrings
import nasa.core.model.ApiKey
import nasa.core.res.CoreStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn

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
        text = ApodStrings.gridToolbarTitle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      if (showBackButton) {
        IconButton(onClick = { onAction(ApodGridAction.NavBack) }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = CoreStrings.navBack,
          )
        }
      }
    },
    actions = {
      IconButton(onClick = { onAction(ApodGridAction.LoadRandom) }) {
        Icon(
          imageVector = Icons.Filled.Shuffle,
          contentDescription = ApodStrings.gridToolbarRandom,
        )
      }
      IconButton(onClick = { onAction(ApodGridAction.SearchMonth(state.dateOrNull())) }) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = ApodStrings.gridToolbarSearch,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewToolbar() = PreviewColumn {
  ApodGridTopBar(
    state = GridScreenState.Success(
      persistentListOf(PREVIEW_ITEM_1, PREVIEW_ITEM_2),
      ApiKey.DEMO,
    ),
    showBackButton = true,
    onAction = {},
  )
}
