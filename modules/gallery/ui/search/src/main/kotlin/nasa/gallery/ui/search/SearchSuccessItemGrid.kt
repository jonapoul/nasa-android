package nasa.gallery.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nasa.core.res.CoreDimens
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.model.MediaType
import nasa.gallery.vm.search.SearchResultItem

@Stable
@Composable
internal fun SearchSuccessItemGrid(
  item: SearchResultItem,
  columnWidth: Dp,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val padding = CoreDimens.medium

  ItemThumbnail(
    modifier = modifier.clickable { onAction(SearchAction.NavToImage(item.nasaId)) },
    item = item,
    theme = theme,
    sizeModifier = { isLoading ->
      (if (isLoading) size(columnWidth - padding) else this)
        .padding(padding)
    },
  )
}

@Preview
@Composable
private fun PreviewAudio() = PreviewColumn {
  SearchSuccessItemGrid(
    item = PREVIEW_ITEM_1.copy(mediaType = MediaType.Audio),
    columnWidth = 150.dp,
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewImage() = PreviewColumn {
  SearchSuccessItemGrid(
    item = PREVIEW_ITEM_1.copy(mediaType = MediaType.Image),
    columnWidth = 150.dp,
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewVideo() = PreviewColumn {
  SearchSuccessItemGrid(
    item = PREVIEW_ITEM_1.copy(mediaType = MediaType.Video),
    columnWidth = 150.dp,
    onAction = {},
  )
}
