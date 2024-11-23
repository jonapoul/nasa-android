package nasa.apod.ui.single

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nasa.apod.preview.PREVIEW_ITEM_1
import nasa.apod.res.ApodStrings
import nasa.apod.vm.single.ScreenState
import nasa.apod.vm.single.dateOrNull
import nasa.core.model.ApiKey
import nasa.core.res.CoreStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn

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
        text = ApodStrings.singleToolbarTitle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = {
      if (showBackButton) {
        IconButton(onClick = { onAction(ApodSingleAction.NavBack) }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = CoreStrings.navBack,
          )
        }
      }
    },
    actions = {
      if (date != null) {
        IconButton(onClick = { onAction(ApodSingleAction.NavGrid(date)) }) {
          Icon(
            imageVector = Icons.Filled.GridView,
            contentDescription = ApodStrings.singleToolbarGrid,
          )
        }
      }

      if (key != null) {
        IconButton(onClick = { onAction(ApodSingleAction.LoadRandom) }) {
          Icon(
            imageVector = Icons.Filled.Shuffle,
            contentDescription = ApodStrings.singleToolbarRandom,
          )
        }
      }

      IconButton(onClick = { onAction(ApodSingleAction.SearchDate(state.dateOrNull())) }) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = ApodStrings.singleToolbarSearch,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewSuccess() = PreviewColumn {
  ApodSingleTopBar(
    state = ScreenState.Success(PREVIEW_ITEM_1, ApiKey.DEMO),
    showBackButton = true,
    onAction = {},
  )
}
