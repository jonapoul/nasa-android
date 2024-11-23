package nasa.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import nasa.core.res.CoreDimens
import nasa.core.ui.CardShape
import nasa.core.ui.ShimmerBlockShape
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.shimmer
import nasa.home.res.HomeStrings

@Composable
private fun HomeGridItem(
  title: String,
  description: String,
  thumbnailUrl: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(CoreDimens.medium)
      .background(theme.cardBackground, CardShape)
      .padding(CoreDimens.medium)
      .clickable { onClick() },
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .wrapContentHeight(),
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = theme.pageTextPrimary,
      )

      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(CoreDimens.large),
        text = description,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Light,
        overflow = TextOverflow.Ellipsis,
        fontSize = 13.sp,
        lineHeight = 13.sp,
      )
    }

    HomeGridImage(
      modifier = Modifier
        .size(IMAGE_SIZE)
        .padding(CoreDimens.large),
      title = title,
      thumbnailUrl = thumbnailUrl,
      theme = theme,
    )
  }
}

@Composable
private fun HomeGridImage(
  thumbnailUrl: String?,
  title: String,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier.clip(ShimmerBlockShape),
  ) {
    var isLoading by remember { mutableStateOf(true) }
    if (thumbnailUrl == null && isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .shimmer(theme, durationMillis = 1000),
      )
    }

    if (thumbnailUrl != null) {
      val fallback = rememberVectorPainter(Icons.Filled.Warning)
      AsyncImage(
        modifier = Modifier.fillMaxSize(),
        model = thumbnailUrl,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        fallback = fallback,
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { isLoading = false },
      )
    }
  }
}

@Composable
internal fun ApodGridItem(
  thumbnailUrl: String?,
  onAction: (HomeAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  HomeGridItem(
    modifier = modifier,
    title = HomeStrings.navApod,
    description = HomeStrings.navApodDesc,
    thumbnailUrl = thumbnailUrl,
    onClick = { onAction(HomeAction.NavApodToday) },
    theme = theme,
  )
}

@Composable
internal fun GalleryGridItem(
  thumbnailUrl: String?,
  onAction: (HomeAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  HomeGridItem(
    modifier = modifier,
    title = HomeStrings.navGallery,
    description = HomeStrings.navGalleryDesc,
    thumbnailUrl = thumbnailUrl,
    onClick = { onAction(HomeAction.NavGallery) },
    theme = theme,
  )
}

private val IMAGE_SIZE = 150.dp

@Preview
@Composable
private fun PreviewApod() = PreviewColumn {
  ApodGridItem(
    thumbnailUrl = PREVIEW_URL_APOD,
    onAction = { },
  )
}

@Preview
@Composable
private fun PreviewGallery() = PreviewColumn {
  GalleryGridItem(
    thumbnailUrl = PREVIEW_URL_GALLERY,
    onAction = { },
  )
}
