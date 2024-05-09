package nasa.gallery.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.getViewModel
import nasa.gallery.search.vm.SearchViewModel

class GallerySearchScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<SearchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreenImpl(
      state = state,
      onAction = { action ->
//        when (action) {
//
//        }
      },
    )
  }
}
