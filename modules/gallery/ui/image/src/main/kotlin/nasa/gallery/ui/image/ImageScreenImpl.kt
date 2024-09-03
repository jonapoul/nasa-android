package nasa.gallery.ui.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nasa.core.model.Percent
import nasa.core.model.percent
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.core.ui.screens.LoadFailure
import nasa.gallery.vm.image.ImageState

@Composable
internal fun ImageScreenImpl(
  imageState: ImageState,
  progress: Percent,
  onAction: (ImageAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  Scaffold(
    topBar = { ImageTopBar(imageState, onAction, theme) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      ImageContent(
        imageState = imageState,
        progress = progress,
        onAction = onAction,
        modifier = Modifier.padding(innerPadding),
        theme = theme,
      )
    }
  }
}

@Composable
private fun ImageContent(
  imageState: ImageState,
  progress: Percent,
  onAction: (ImageAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val contentsModifier = modifier
    .fillMaxWidth()
    .wrapContentHeight()

  when (imageState) {
    ImageState.Loading -> ImageLoading(
      modifier = contentsModifier.padding(64.dp),
      theme = theme,
    )

    is ImageState.Failed -> LoadFailure(
      modifier = contentsModifier.padding(32.dp),
      message = imageState.reason,
      onRetryLoad = { onAction(ImageAction.RetryLoad) },
      theme = theme,
    )

    is ImageState.FoundUrl -> ImageSuccess(
      modifier = contentsModifier,
      url = imageState.imageUrl,
      progress = progress,
      contentDescription = imageState.contentDescription,
      theme = theme,
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ImageScreenImpl(
    imageState = ImageState.Loading,
    progress = 0.percent,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailed() = PreviewScreen {
  ImageScreenImpl(
    imageState = ImageState.Failed(reason = "Something broke lol"),
    progress = 0.percent,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ImageScreenImpl(
    imageState = ImageState.FoundUrl(
      imageUrl = PREVIEW_URL,
      title = "Hello world",
      contentDescription = "whatever",
    ),
    progress = 69.percent,
    onAction = {},
  )
}
