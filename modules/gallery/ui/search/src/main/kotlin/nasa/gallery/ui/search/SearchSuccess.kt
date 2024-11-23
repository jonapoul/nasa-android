package nasa.gallery.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import my.nanihadesuka.compose.LazyColumnScrollbar
import nasa.core.res.CoreDimens
import nasa.core.ui.button.PrimaryIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.scrollbarSettings
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.model.NasaId
import nasa.gallery.model.SearchViewConfig
import nasa.gallery.model.SearchViewType
import nasa.gallery.res.GalleryStrings
import nasa.gallery.vm.search.SearchResultItem
import nasa.gallery.vm.search.SearchState

@Composable
internal fun SearchSuccess(
  state: SearchState.Success,
  config: SearchViewConfig,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val estimatedNumPages = remember(state) {
    // E.g. pageNumber=3, resultsPerPage=100, totalResults=242 => estimatedNumPages=3
    val modulus = state.totalResults % state.resultsPerPage
    (state.totalResults / state.resultsPerPage) + (if (modulus == 0) 0 else 1)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = CoreDimens.medium),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(CoreDimens.large),
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.displaySmall,
      text = GalleryStrings.searchResultsHeader(state.totalResults),
    )

    when (config.type) {
      SearchViewType.Grid -> SearchSuccessContentGrid(
        modifier = Modifier.weight(1f),
        results = state.results,
        columnWidth = config.columnWidthDp.dp,
        onAction = onAction,
        theme = theme,
      )

      SearchViewType.Card -> SearchSuccessContentCard(
        modifier = Modifier.weight(1f),
        results = state.results,
        onAction = onAction,
        theme = theme,
      )
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(CoreDimens.medium),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val previousPage = state.prevPageNumber
      PrimaryIconButton(
        imageVector = Icons.Filled.ArrowBackIosNew,
        contentDescription = GalleryStrings.searchPagePrevious,
        enabled = previousPage != null,
        onClick = { if (previousPage != null) onAction(SearchAction.SelectPage(previousPage)) },
      )

      Text(
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        text = GalleryStrings.searchResultsPage(state.pageNumber, estimatedNumPages),
      )

      val nextPage = state.nextPageNumber
      PrimaryIconButton(
        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
        contentDescription = GalleryStrings.searchPageNext,
        enabled = nextPage != null,
        onClick = { if (nextPage != null) onAction(SearchAction.SelectPage(nextPage)) },
      )
    }
  }
}

@Composable
private fun SearchSuccessContentCard(
  results: ImmutableList<SearchResultItem>,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val listState = rememberLazyListState()
  LazyColumnScrollbar(
    modifier = modifier,
    state = listState,
    settings = theme.scrollbarSettings(),
  ) {
    LazyColumn(
      state = listState,
      verticalArrangement = Arrangement.Top,
    ) {
      items(results, key = { it.nasaId.value }) { item ->
        SearchSuccessItemCard(
          modifier = Modifier.fillMaxWidth(),
          item = item,
          onAction = onAction,
          theme = theme,
        )
      }
    }
  }
}

// TODO: Get a scroll bar?
@Composable
private fun SearchSuccessContentGrid(
  results: ImmutableList<SearchResultItem>,
  columnWidth: Dp,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val gridState = rememberLazyStaggeredGridState()

  LazyVerticalStaggeredGrid(
    state = gridState,
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    columns = StaggeredGridCells.FixedSize(columnWidth),
  ) {
    items(results, key = { it.nasaId.value }) { item ->
      SearchSuccessItemGrid(
        modifier = Modifier.fillMaxWidth(),
        item = item,
        columnWidth = columnWidth,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewCard() = PreviewScreen {
  SearchSuccess(
    state = PREVIEW_SUCCESS_STATE,
    config = PREVIEW_CARD_CONFIG,
    onAction = {},
  )
}

@ScreenPreview
@Suppress("MagicNumber")
@Composable
private fun PreviewGrid() = PreviewScreen {
  val template = PREVIEW_SUCCESS_STATE.results.first()
  val a = 'a'.code

  val items = (0..25)
    .map { (a + it).toChar() }
    .map { template.copy(nasaId = NasaId(it.toString())) }
    .toImmutableList()

  SearchSuccess(
    state = PREVIEW_SUCCESS_STATE.copy(results = items),
    config = PREVIEW_GRID_CONFIG,
    onAction = {},
  )
}
