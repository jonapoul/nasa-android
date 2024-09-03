package nasa.apod.ui.full

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.apod.preview.PREVIEW_DATE
import nasa.apod.preview.PREVIEW_ITEM_1
import nasa.apod.res.R
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn

@Composable
internal fun ApodFullScreenTopBar(
  date: LocalDate,
  item: ApodItem?,
  theme: Theme,
  onClickedBack: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    TopAppBar(
      colors = theme.topAppBarColors(),
      title = {
        item ?: return@TopAppBar
        Text(
          text = item.title,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      },
      navigationIcon = {
        IconButton(onClick = onClickedBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.apod_fullscreen_back),
          )
        }
      },
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(theme.toolbarBackgroundSubdued)
        .padding(4.dp),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = date.toString(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = theme.toolbarTextSubdued,
      )
    }
  }
}

@Preview
@Composable
private fun PreviewFull() = PreviewColumn {
  ApodFullScreenTopBar(
    date = PREVIEW_DATE,
    item = PREVIEW_ITEM_1,
    theme = LocalTheme.current,
    onClickedBack = {},
  )
}

@Preview
@Composable
private fun PreviewNullItem() = PreviewColumn {
  ApodFullScreenTopBar(
    date = PREVIEW_DATE,
    item = null,
    theme = LocalTheme.current,
    onClickedBack = {},
  )
}
