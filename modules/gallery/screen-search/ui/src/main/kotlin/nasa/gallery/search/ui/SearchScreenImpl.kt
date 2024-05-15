@file:Suppress("UNUSED_PARAMETER")

package nasa.gallery.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.search.vm.SearchScreenState

@Composable
internal fun SearchScreenImpl(
  state: SearchScreenState,
  onAction: (SearchAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { SearchTopBar(onAction, theme) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      SearchContent(
        modifier = Modifier.padding(innerPadding),
        state = state,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun SearchContent(
  state: SearchScreenState,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    contentAlignment = Alignment.Center,
  ) {
    when (state) {
      is SearchScreenState.Empty -> {
        SearchEmpty(
          theme = theme,
        )
      }

      SearchScreenState.Searching -> {
        // TBC
      }

      is SearchScreenState.Failed -> {
        // TBC
      }

      is SearchScreenState.Success -> {
        // TBC
      }
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  SearchScreenImpl(
    state = SearchScreenState.Success(
      persistentListOf(EXAMPLE_ITEM_1),
    ),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  SearchScreenImpl(
    state = SearchScreenState.Failed(reason = "Something broke! Here's some more rubbish too for preview"),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  SearchScreenImpl(
    state = SearchScreenState.Searching,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewEmpty() = PreviewScreen {
  SearchScreenImpl(
    state = SearchScreenState.Empty,
    onAction = {},
  )
}
