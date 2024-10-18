package nasa.gallery.ui.search

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nasa.core.ui.CardShape
import nasa.core.ui.Dimensions
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.model.MediaType
import nasa.gallery.res.R
import nasa.gallery.vm.search.SearchResultItem

@Stable
@Composable
internal fun SearchSuccessItemCard(
  item: SearchResultItem,
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(Dimensions.Medium)
      .background(theme.cardBackground, CardShape)
      .padding(Dimensions.Large)
      .clickable { onAction(SearchAction.NavToImage(item.nasaId)) },
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.Top,
  ) {
    ItemThumbnail(
      item = item,
      theme = theme,
      sizeModifier = { size(PREVIEW_SIZE) },
    )

    HorizontalSpacer(Dimensions.Large)

    Column(
      modifier = Modifier
        .weight(1f)
        .wrapContentHeight(),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = item.title,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        color = theme.pageText,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = LARGER_TEXT_SIZE,
        lineHeight = LARGER_TEXT_SIZE,
      )

      TitleAndValue(
        title = stringResource(id = R.string.search_item_date),
        value = item.dateCreated,
        stringifier = { dateFormat(it) },
        theme = theme,
      )

      TitleAndValue(
        title = stringResource(id = R.string.search_item_type),
        value = item.mediaType,
        stringifier = { it.string() },
        theme = theme,
      )

      TitleAndValue(
        title = stringResource(id = R.string.search_item_center),
        value = item.center,
        theme = theme,
      )

      TitleAndValue(
        title = stringResource(id = R.string.search_item_location),
        value = item.location,
        theme = theme,
      )

      TitleAndValue(
        title = stringResource(id = R.string.search_item_photographer),
        value = item.photographer,
        theme = theme,
      )

      TitleAndValue(
        title = stringResource(id = R.string.search_item_description),
        value = item.description508,
        theme = theme,
        maxLines = 5,
      )
    }
  }
}

@Stable
@Composable
private fun <T : Any> TitleAndValue(
  title: String,
  value: T?,
  modifier: Modifier = Modifier,
  stringifier: @Composable (T) -> String = { it.toString() },
  maxLines: Int = 2,
  theme: Theme = LocalTheme.current,
) {
  value ?: return
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.Top,
  ) {
    Text(
      modifier = Modifier.wrapContentWidth(),
      text = title,
      textAlign = TextAlign.Start,
      fontSize = SMALLER_TEXT_SIZE,
      color = theme.pageTextSubdued,
      fontWeight = FontWeight.Bold,
      lineHeight = SMALLER_TEXT_SIZE,
    )

    HorizontalSpacer(Dimensions.Medium)

    Text(
      modifier = Modifier.weight(1f),
      text = stringifier(value),
      textAlign = TextAlign.End,
      fontSize = SMALLER_TEXT_SIZE,
      color = theme.pageTextSubdued,
      maxLines = maxLines,
      overflow = TextOverflow.Ellipsis,
      lineHeight = SMALLER_TEXT_SIZE,
    )
  }
}

@Stable
@Composable
private fun MediaType.string() = stringResource(
  id = when (this) {
    MediaType.Audio -> R.string.media_type_audio
    MediaType.Image -> R.string.media_type_image
    MediaType.Video -> R.string.media_type_video
  },
)

@Stable
@Composable
private fun dateFormat(instant: Instant): String {
  val tz = remember { TimeZone.currentSystemDefault() }
  val date = instant.toLocalDateTime(tz).date
  return LocalDate.Formats.ISO.format(date)
}

internal val PREVIEW_SIZE = 128.dp
private val SMALLER_TEXT_SIZE = 12.sp
private val LARGER_TEXT_SIZE = 14.sp

@Preview
@Composable
private fun PreviewItem() = PreviewColumn {
  SearchSuccessItemCard(
    item = PREVIEW_ITEM_1,
    onAction = {},
  )
}
