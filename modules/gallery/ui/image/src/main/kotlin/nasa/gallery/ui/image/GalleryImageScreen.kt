package nasa.gallery.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import nasa.core.ui.color.LocalTheme
import nasa.gallery.model.NasaId
import nasa.gallery.vm.image.ImageViewModel

@Composable
fun GalleryImageScreen(
  id: NasaId,
  navController: NavController,
  viewModel: ImageViewModel = hiltViewModel(),
) {
  val theme = LocalTheme.current
  val imageState by viewModel.imageState.collectAsStateWithLifecycle()
  val progress by viewModel.progress.collectAsStateWithLifecycle()

  LaunchedEffect(id) { viewModel.load(id) }

  ImageScreenImpl(
    imageState = imageState,
    progress = progress,
    theme = theme,
    onAction = { action ->
      when (action) {
        ImageAction.NavBack -> navController.popBackStack()
        ImageAction.RetryLoad -> viewModel.reload(id)
        ImageAction.LoadMetadata -> viewModel.loadMetadata(id)
      }
    },
  )
}
