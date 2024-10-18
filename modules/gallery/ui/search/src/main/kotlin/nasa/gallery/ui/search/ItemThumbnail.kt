package nasa.gallery.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import nasa.core.ui.ShimmerBlockShape
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.shimmer
import nasa.gallery.model.MediaType
import nasa.gallery.vm.search.SearchResultItem

@Stable
@Composable
internal fun ItemThumbnail(
  item: SearchResultItem,
  sizeModifier: Modifier.(isLoading: Boolean) -> Modifier,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val previewUrl = item.previewUrl
  if (previewUrl == null) {
    PlaceholderImage(
      modifier = Modifier
        .size(PREVIEW_SIZE)
        .clip(ShimmerBlockShape),
      item = item,
      theme = theme,
    )
  } else {
    Box(
      modifier = modifier,
    ) {
      var isLoading by remember { mutableStateOf(true) }
      if (isLoading) {
        Box(
          modifier = Modifier
            .sizeModifier(isLoading)
            .clip(ShimmerBlockShape)
            .shimmer(theme, durationMillis = 1000),
        )
      }

      val fallback = rememberVectorPainter(Icons.Filled.Warning)
      AsyncImage(
        modifier = Modifier.sizeModifier(isLoading),
        model = previewUrl.url,
        contentDescription = item.title,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center,
        fallback = fallback,
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { isLoading = false },
      )
    }
  }
}

@Stable
@Composable
private fun PlaceholderImage(
  item: SearchResultItem,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Image(
    modifier = modifier.size(PREVIEW_SIZE),
    imageVector = when (item.mediaType) {
      MediaType.Audio -> Icons.Filled.Audiotrack
      MediaType.Image -> Icons.Filled.Image
      MediaType.Video -> Icons.Filled.OndemandVideo
    },
    contentDescription = item.title,
    colorFilter = ColorFilter.tint(theme.pageText),
  )
}

@Preview
@Composable
private fun PreviewAudio() = PreviewColumn {
  PlaceholderImage(
    item = PREVIEW_ITEM_1.copy(mediaType = MediaType.Audio),
  )
}

@Preview
@Composable
private fun PreviewImage() = PreviewColumn {
  PlaceholderImage(
    item = PREVIEW_ITEM_1.copy(mediaType = MediaType.Image),
  )
}

@Preview
@Composable
private fun PreviewVideo() = PreviewColumn {
  PlaceholderImage(
    item = PREVIEW_ITEM_1.copy(mediaType = MediaType.Video),
  )
}
