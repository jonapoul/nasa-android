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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.LazyColumnScrollbar
import nasa.core.ui.button.PrimaryIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.scrollbarSettings
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.res.R

@Composable
internal fun SearchSuccess(
  state: SearchState.Success,
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
      .padding(horizontal = PADDING),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.displaySmall,
      text = stringResource(R.string.search_results_header, state.totalResults),
    )

    val listState = rememberLazyListState()
    LazyColumnScrollbar(
      modifier = Modifier.weight(1f),
      state = listState,
      settings = theme.scrollbarSettings(),
    ) {
      LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.Top,
      ) {
        items(state.results) { item ->
          SearchSuccessItem(
            modifier = Modifier.fillMaxWidth(),
            item = item,
            onAction = onAction,
            theme = theme,
          )
        }
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(PADDING),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val previousPage = state.prevPageNumber
      PrimaryIconButton(
        imageVector = Icons.Filled.ArrowBackIosNew,
        contentDescription = stringResource(R.string.search_page_previous),
        enabled = previousPage != null,
        onClick = { if (previousPage != null) onAction(SearchAction.SelectPage(previousPage)) },
      )

      Text(
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        text = stringResource(R.string.search_results_page, state.pageNumber, estimatedNumPages),
      )

      val nextPage = state.nextPageNumber
      PrimaryIconButton(
        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
        contentDescription = stringResource(R.string.search_page_next),
        enabled = nextPage != null,
        onClick = { if (nextPage != null) onAction(SearchAction.SelectPage(nextPage)) },
      )
    }
  }
}

private val PADDING = 4.dp

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  SearchSuccess(
    state = PreviewSuccessState,
    onAction = {},
  )
}
