package nasa.gallery.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
      .weight(1f)

    when (searchState) {
      SearchState.Empty -> SearchEmpty(
        modifier = contentsModifier,
        theme = theme,
      )

      SearchState.Searching -> SearchSearching(
        modifier = contentsModifier,
        theme = theme,
      )

      is SearchState.Failed -> LoadFailure(
        modifier = contentsModifier,
        message = searchState.reason,
        onRetryLoad = { onAction(SearchAction.RetrySearch) },
        theme = theme,
      )

      is SearchState.Success -> SearchSuccess(
        modifier = contentsModifier,
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
      persistentListOf(EXAMPLE_ITEM_1),
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