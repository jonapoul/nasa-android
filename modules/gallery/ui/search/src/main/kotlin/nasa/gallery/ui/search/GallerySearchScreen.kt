package nasa.gallery.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.set
import nasa.gallery.model.NasaId
import nasa.gallery.nav.GalleryImageNavScreen
import nasa.gallery.vm.search.SearchViewModel

@Composable
fun GallerySearchScreen(
  navController: NavController,
  viewModel: SearchViewModel = hiltViewModel(),
) {
  val theme = LocalTheme.current
  val searchState by viewModel.searchState.collectAsStateWithLifecycle()
  val filterConfig by viewModel.filterConfig.collectAsStateWithLifecycle()
  val viewConfig by viewModel.viewConfig.collectAsStateWithLifecycle()

  val clickedImageId = remember { mutableStateOf<NasaId?>(null) }
  val showExtraConfig = remember { mutableStateOf(false) }
  val immutableClickedImageId = clickedImageId.value
  if (immutableClickedImageId != null) {
    navController.navigate(route = GalleryImageNavScreen(immutableClickedImageId.value))
    clickedImageId.set(null)
  }

  val showViewConfigModal = remember { mutableStateOf(false) }

  SearchScreenImpl(
    searchState = searchState,
    theme = theme,
    filterConfig = filterConfig,
    config = viewConfig,
    showExtraConfig = showExtraConfig.value,
    showViewConfigModal = showViewConfigModal.value,
    onAction = { action ->
      when (action) {
        SearchAction.NavBack -> navController.popBackStack()
        is SearchAction.NavToImage -> clickedImageId.set(action.id)
        is SearchAction.EnterSearchTerm -> viewModel.enterSearchTerm(action.text)
        SearchAction.PerformSearch -> viewModel.performSearch()
        is SearchAction.SelectPage -> viewModel.performSearch(action.pageNumber)
        is SearchAction.SetFilterConfig -> viewModel.setFilterConfig(action.config)
        SearchAction.ToggleExtraConfig -> showExtraConfig.set(!showExtraConfig.value)
        SearchAction.ResetExtraConfig -> viewModel.resetExtraConfig()
        is SearchAction.ShowViewConfigDialog -> showViewConfigModal.set(true)
        SearchAction.HideViewTypeDialog -> showViewConfigModal.set(false)
        SearchAction.ResetViewConfig -> viewModel.resetViewConfig()
        is SearchAction.SetViewConfig -> viewModel.setViewConfig(action.config)
      }
    },
  )
}
