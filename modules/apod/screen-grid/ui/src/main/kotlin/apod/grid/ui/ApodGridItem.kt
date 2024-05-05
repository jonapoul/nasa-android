package apod.grid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.model.ApodItem
import apod.core.model.ApodMediaType
import apod.core.ui.ShimmerBlockShape
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewColumn
import apod.core.ui.screens.VideoOverlay
import apod.core.ui.shimmer
import apod.grid.vm.ApodGridAction
import coil.compose.AsyncImage

@Composable
internal fun ApodGridItem(
  item: ApodItem,
  onAction: (ApodGridAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .width(ITEM_SIZE)
      .padding(8.dp)
      .clickable { onAction(ApodGridAction.NavToItem(item)) },
    contentAlignment = Alignment.BottomCenter,
  ) {
    var isLoading by remember { mutableStateOf(true) }
    if (isLoading) {
      Box(
        modifier = Modifier
          .size(ITEM_SIZE)
          .clip(ShimmerBlockShape)
          .shimmer(theme, durationMillis = 1000),
      )
    }

    val fallback = rememberVectorPainter(Icons.Filled.Warning)
    AsyncImage(
      modifier = Modifier.size(ITEM_SIZE),
      model = item.thumbnailUrl ?: item.url,
      contentDescription = item.title,
      contentScale = ContentScale.Inside,
      alignment = Alignment.Center,
      fallback = fallback,
      onLoading = { isLoading = true },
      onSuccess = { isLoading = false },
      onError = { isLoading = false },
    )

    VideoOverlay(
      item = item,
      modifier = Modifier.size(ITEM_SIZE),
      theme = theme,
    )

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(theme.cardBackground.copy(alpha = 0.6f))
        .padding(8.dp),
    ) {
      Text(
        text = item.title,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        color = theme.pageTextPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = item.date.toString(),
        fontSize = 12.sp,
        lineHeight = 15.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Preview
@Composable
private fun PreviewGridItem() = PreviewColumn {
  ApodGridItem(
    item = EXAMPLE_ITEM_1.copy(title = "Even longer title to show ellipsis at the end"),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewVideoItem() = PreviewColumn {
  ApodGridItem(
    item = EXAMPLE_ITEM_1.copy(
      thumbnailUrl = EXAMPLE_ITEM_1.url,
      mediaType = ApodMediaType.Video,
    ),
    onAction = {},
  )
}

internal val ITEM_SIZE = 150.dp
