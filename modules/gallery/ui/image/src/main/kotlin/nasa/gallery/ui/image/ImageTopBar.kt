package nasa.gallery.ui.image

import alakazam.kotlin.core.noOp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.res.CoreStrings
import nasa.core.ui.ShimmeringBlock
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.topAppBarColors
import nasa.core.ui.preview.PreviewColumn
import nasa.gallery.vm.image.ImageState

@Composable
internal fun ImageTopBar(
  state: ImageState,
  onAction: (ImageAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  TopAppBar(
    colors = theme.topAppBarColors(),
    title = {
      LoadableTitleText(
        state = state,
        theme = theme,
      )
    },
    navigationIcon = {
      IconButton(onClick = { onAction(ImageAction.NavBack) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = CoreStrings.navBack,
        )
      }
    },
  )
}

@Composable
private fun LoadableTitleText(
  state: ImageState,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  when (state) {
    is ImageState.Failed -> noOp()

    is ImageState.FoundUrl -> Text(
      modifier = modifier,
      text = state.title,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )

    ImageState.Loading -> ShimmeringBlock(
      modifier = modifier
        .fillMaxWidth()
        .height(45.dp),
      theme = theme,
    )
  }
}

@Preview
@Composable
private fun PreviewFailed() = PreviewColumn {
  ImageTopBar(
    state = ImageState.Failed(reason = "whatever"),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewSuccess() = PreviewColumn {
  ImageTopBar(
    state = ImageState.FoundUrl(
      title = "Hello world",
      imageUrl = PREVIEW_URL,
      contentDescription = "blah",
    ),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewLoading() = PreviewColumn {
  ImageTopBar(
    state = ImageState.Loading,
    onAction = {},
  )
}
