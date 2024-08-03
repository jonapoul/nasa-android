package nasa.gallery.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import nasa.core.ui.screens.LoadFailure
import nasa.gallery.search.vm.SearchState

@Composable
internal fun SearchScreenImpl(
  searchState: SearchState,
  onAction: (SearchAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  Scaffold(
    topBar = { SearchTopBar(onAction, theme) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      SearchContent(
        searchState = searchState,
        onAction = onAction,
        modifier = Modifier.padding(innerPadding),
        theme = theme,
      )
    }
  }
}

@Composable
private fun SearchContent(
  searchState: SearchState,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    SearchInput(
      modifier = Modifier.fillMaxWidth(),
      onAction = onAction,
      theme = theme,
    )

    val contentsModifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()

    when (searchState) {
      SearchState.NoAction -> SearchNoAction(
        modifier = contentsModifier.padding(64.dp),
        theme = theme,
      )

      SearchState.Empty -> SearchEmpty(
        modifier = contentsModifier.padding(64.dp),
        theme = theme,
      )

      SearchState.Searching -> SearchSearching(
        modifier = contentsModifier.padding(64.dp),
        theme = theme,
      )

      is SearchState.Failed -> LoadFailure(
        modifier = contentsModifier.padding(32.dp),
        message = searchState.reason,
        onRetryLoad = { onAction(SearchAction.RetrySearch) },
        theme = theme,
      )

      is SearchState.Success -> SearchSuccess(
        modifier = contentsModifier.weight(1f),
        items = searchState.results,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Success(
      persistentListOf(EXAMPLE_ITEM_1, EXAMPLE_ITEM_2),
    ),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Failed(reason = "Something broke! Here's some more rubbish too for preview"),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Searching,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewEmpty() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Empty,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoAction() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.NoAction,
    onAction = {},
  )
}
