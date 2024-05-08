package nasa.gallery.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.vm.GalleryScreenState

@Composable
internal fun GalleryScreenImpl(
  state: GalleryScreenState,
  onAction: (GalleryAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { GalleryTopBar(state, onAction, theme) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      GalleryContent(
        modifier = Modifier.padding(innerPadding),
        state = state,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun GalleryContent(
  state: GalleryScreenState,
  onAction: (GalleryAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    contentAlignment = Alignment.Center,
  ) {
    // TBC
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  GalleryScreenImpl(
    state = GalleryScreenState.Success(EXAMPLE_KEY),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  GalleryScreenImpl(
    state = GalleryScreenState.Failed(
      EXAMPLE_KEY,
      message = "Something broke! Here's some more rubbish too for preview",
    ),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  GalleryScreenImpl(
    state = GalleryScreenState.Loading(EXAMPLE_KEY),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoKey() = PreviewScreen {
  GalleryScreenImpl(
    state = GalleryScreenState.NoApiKey,
    onAction = {},
  )
}
