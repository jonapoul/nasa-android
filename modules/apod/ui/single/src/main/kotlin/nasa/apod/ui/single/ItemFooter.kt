package nasa.apod.ui.single

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import nasa.apod.model.ApodItem
import nasa.apod.preview.PREVIEW_ITEM_1
import nasa.apod.res.R
import nasa.core.ui.CardShape
import nasa.core.ui.Dimensions
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn

@Composable
internal fun ItemFooter(
  item: ApodItem,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .background(theme.cardBackground, CardShape)
      .clickable { onAction(ApodSingleAction.ShowDescriptionDialog(item)) }
      .padding(Dimensions.Large),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val copyright = item.copyright
    if (copyright != null) {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.apod_single_copyright, copyright),
        fontWeight = FontWeight.Bold,
        color = theme.pageTextPrimary,
      )
    }

    Text(
      text = item.explanation,
      color = theme.pageText,
      fontSize = 13.sp,
      maxLines = 7,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Preview
@Composable
private fun PreviewRegular() = PreviewColumn {
  ItemFooter(
    item = PREVIEW_ITEM_1,
    onAction = {},
  )
}
