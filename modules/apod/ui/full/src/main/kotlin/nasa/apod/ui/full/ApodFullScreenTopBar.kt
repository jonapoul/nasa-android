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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.apod.res.ApodStrings
import nasa.core.res.CoreDimens
import nasa.core.ui.color.Theme

@Composable
internal fun ApodFullScreenTopBar(
  date: LocalDate,
  item: ApodItem?,
  hazeState: HazeState,
  hazeStyle: HazeStyle,
  theme: Theme,
  onClickedBack: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    TopAppBar(
      modifier = Modifier.hazeChild(hazeState, hazeStyle),
      colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
      ),
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
            contentDescription = ApodStrings.fullscreenBack,
          )
        }
      },
    )

    Box(
      modifier = Modifier
        .hazeChild(hazeState, hazeStyle)
        .fillMaxWidth()
        .background(Color.Transparent)
        .padding(CoreDimens.medium),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = date.toString(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = theme.pageText,
      )
    }
  }
}
