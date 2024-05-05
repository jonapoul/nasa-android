package apod.single.ui

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
import apod.core.model.ApodItem
import apod.core.ui.color.Theme
import apod.core.ui.color.topAppBarColors

@Composable
internal fun ApodFullScreenTopBar(
  item: ApodItem,
  theme: Theme,
  onClickedBack: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
  ) {
    TopAppBar(
      colors = theme.topAppBarColors(),
      title = {
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
        text = item.date.toString(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = theme.toolbarTextSubdued,
      )
    }
  }
}
