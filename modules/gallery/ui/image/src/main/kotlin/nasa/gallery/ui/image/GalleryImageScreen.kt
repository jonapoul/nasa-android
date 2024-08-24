package nasa.gallery.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.getViewModel
import nasa.gallery.model.NasaId

class GalleryImageScreen(
  private val id: NasaId,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current
    val viewModel = getViewModel<ImageViewModel>()

    val imageState by viewModel.imageState.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    ImageScreenImpl(
      imageState = imageState,
      progress = progress,
      theme = theme,
      onAction = { action ->
        when (action) {
          ImageAction.NavBack -> navigator.pop()
          ImageAction.RetryLoad -> viewModel.reload(id)
          ImageAction.LoadMetadata -> viewModel.loadMetadata(id)
        }
      },
    )
  }
}
