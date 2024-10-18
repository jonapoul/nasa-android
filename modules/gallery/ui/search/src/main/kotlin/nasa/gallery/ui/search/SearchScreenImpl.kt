@file:Suppress("UnusedPrivateMember")

package nasa.gallery.ui.search

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
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.core.ui.screens.LoadFailure
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.SearchViewConfig
import nasa.gallery.vm.search.SearchState

@Composable
internal fun SearchScreenImpl(
  searchState: SearchState,
  filterConfig: FilterConfig,
  config: SearchViewConfig,
  showExtraConfig: Boolean,
  showViewConfigModal: Boolean,
  onAction: (SearchAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  Scaffold(
    topBar = {
      SearchTopBar(
        onAction = onAction,
        theme = theme,
      )
    },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      SearchContent(
        searchState = searchState,
        viewConfig = config,
        onAction = onAction,
        modifier = Modifier.padding(innerPadding),
        theme = theme,
      )

      if (showExtraConfig) {
        SearchConfigModal(
          config = filterConfig,
          onAction = onAction,
          theme = theme,
        )
      }

      if (showViewConfigModal) {
        ViewConfigModal(
          config = config,
          onAction = onAction,
          theme = theme,
        )
      }
    }
  }
}

@Composable
private fun SearchContent(
  searchState: SearchState,
  viewConfig: SearchViewConfig,
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
      SearchState.NoAction, SearchState.NoFilterConfig -> SearchNoAction(
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

      is SearchState.LoadingPage -> SearchLoadingPage(
        modifier = contentsModifier.padding(64.dp),
        theme = theme,
        pageNumber = searchState.pageNumber,
      )

      is SearchState.Failed -> LoadFailure(
        modifier = contentsModifier.padding(32.dp),
        message = searchState.reason,
        onRetryLoad = { onAction(SearchAction.PerformSearch) },
        theme = theme,
      )

      is SearchState.Success -> SearchSuccess(
        modifier = contentsModifier.weight(1f),
        state = searchState,
        config = viewConfig,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccessCard() = PreviewScreen {
  SearchScreenImpl(
    searchState = PREVIEW_SUCCESS_STATE,
    config = PREVIEW_CARD_CONFIG,
    filterConfig = PREVIEW_FILTER_CONFIG,
    showExtraConfig = false,
    showViewConfigModal = false,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewSuccessGrid() = PreviewScreen {
  SearchScreenImpl(
    searchState = PREVIEW_SUCCESS_STATE,
    config = PREVIEW_GRID_CONFIG,
    filterConfig = PREVIEW_FILTER_CONFIG,
    showExtraConfig = false,
    showViewConfigModal = false,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Failed(reason = "Something broke! Here's some more rubbish too for preview"),
    config = PREVIEW_CARD_CONFIG,
    filterConfig = PREVIEW_FILTER_CONFIG,
    showExtraConfig = false,
    showViewConfigModal = false,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Searching,
    config = PREVIEW_CARD_CONFIG,
    filterConfig = PREVIEW_FILTER_CONFIG,
    showExtraConfig = false,
    showViewConfigModal = false,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewEmpty() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.Empty,
    config = PREVIEW_CARD_CONFIG,
    filterConfig = PREVIEW_FILTER_CONFIG,
    showExtraConfig = false,
    showViewConfigModal = false,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoAction() = PreviewScreen {
  SearchScreenImpl(
    searchState = SearchState.NoAction,
    config = PREVIEW_CARD_CONFIG,
    filterConfig = PREVIEW_FILTER_CONFIG,
    showExtraConfig = false,
    showViewConfigModal = false,
    onAction = {},
  )
}
