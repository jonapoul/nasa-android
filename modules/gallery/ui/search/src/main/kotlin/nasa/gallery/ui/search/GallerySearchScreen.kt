@file:SuppressLint("ComposeModifierMissing")
@file:Suppress("ModifierMissing")

package nasa.gallery.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    navController.navigate(route = GalleryImageNavScreen(immutableClickedImageId.value))
    clickedImageId.set(null)
  }

  SearchScreenImpl(
    searchState = searchState,
    theme = theme,
    onAction = { action ->
      when (action) {
        SearchAction.NavBack -> navController.popBackStack()
        is SearchAction.NavToImage -> clickedImageId.set(action.id)
        is SearchAction.EnterSearchTerm -> viewModel.enterSearchTerm(action.text)
        SearchAction.PerformSearch -> viewModel.performSearch()
        SearchAction.ConfigureSearch -> showFilterModal.set(true)
        is SearchAction.SelectPage -> viewModel.performSearch(action.pageNumber)
      }
    },
  )
}
