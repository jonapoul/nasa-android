package nasa.gallery.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import nasa.core.ui.CardShape
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.search.vm.SearchResultItem

@Composable
internal fun SearchSuccess(
  items: ImmutableList<SearchResultItem>,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  LazyColumn(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.Top,
  ) {
    items(items) { item ->
      SearchSuccessItem(
        modifier = Modifier.fillMaxWidth(),
        item = item,
        theme = theme,
      )
    }
  }
}

@Composable
private fun SearchSuccessItem(
  item: SearchResultItem,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(4.dp)
      .background(theme.cardBackground, CardShape)
      .padding(8.dp),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = item.nasaId.toString(),
    )
  }
}

@Preview
@Composable
private fun PreviewItem() = PreviewColumn {
  SearchSuccessItem(
    item = EXAMPLE_ITEM_1,
  )
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  SearchSuccess(
    items = persistentListOf(EXAMPLE_ITEM_1),
    onAction = {},
  )
}
