package nasa.gallery.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.getViewModel
import nasa.gallery.vm.GalleryViewModel

class GalleryScreen : Screen {
  @Suppress("CyclomaticComplexMethod")
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<GalleryViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()

    GalleryScreenImpl(
      state = state,
      onAction = { action ->
//        when (action) {
//
//        }
      },
    )
  }
}
