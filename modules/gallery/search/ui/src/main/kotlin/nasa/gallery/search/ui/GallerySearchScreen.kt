package nasa.gallery.search.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.getViewModel
import nasa.core.ui.set
import nasa.gallery.model.NasaId
import nasa.gallery.nav.GalleryImageNavScreen
import nasa.gallery.search.vm.SearchViewModel

class GallerySearchScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current
    val viewModel = getViewModel<SearchViewModel>()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val filterConfig by viewModel.filterConfig.collectAsStateWithLifecycle()

    val showFilterModal = remember { mutableStateOf(false) }
    if (showFilterModal.value) {
      SearchFilterModal(
        modifier = Modifier.fillMaxWidth(),
        config = filterConfig,
        onConfirm = viewModel::setFilterConfig,
        onClose = { showFilterModal.set(false) },
        theme = theme,
      )
    }

    val clickedImageId = remember { mutableStateOf<NasaId?>(null) }
    val immutableClickedImageId = clickedImageId.value
    if (immutableClickedImageId != null) {
      val imageScreen = rememberScreen(GalleryImageNavScreen(immutableClickedImageId))
      navigator.push(imageScreen)
      clickedImageId.set(null)
    }

    SearchScreenImpl(
      searchState = searchState,
      theme = theme,
      onAction = { action ->
        when (action) {
          SearchAction.NavBack -> navigator.pop()
          is SearchAction.NavToImage -> clickedImageId.set(action.id)
          SearchAction.RetrySearch -> viewModel.retrySearch()
          is SearchAction.EnterSearchTerm -> viewModel.enterSearchTerm(action.text)
          SearchAction.PerformSearch -> viewModel.performSearch()
          SearchAction.ConfigureSearch -> showFilterModal.set(true)
          is SearchAction.SelectPage -> viewModel.performSearch(action.pageNumber)
        }
      },
    )
  }
}
