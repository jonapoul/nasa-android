package nasa.apod.ui.single

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.apod.res.R
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Composable
internal fun ApodFullScreenTopBar(
  date: LocalDate,
  item: ApodItem?,
  theme: Theme,
  onClickedBack: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
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

@ScreenPreview
@Composable
private fun PreviewFull() = PreviewScreen {
  ApodFullScreenTopBar(
    date = EXAMPLE_DATE,
    item = EXAMPLE_ITEM,
    theme = LocalTheme.current,
    onClickedBack = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNullItem() = PreviewScreen {
  ApodFullScreenTopBar(
    date = EXAMPLE_DATE,
    item = null,
    theme = LocalTheme.current,
    onClickedBack = {},
  )
}
