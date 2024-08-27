package nasa.gallery.ui.search

import androidx.compose.runtime.Immutable
import nasa.gallery.model.NasaId

@Immutable
internal sealed interface SearchAction {
  data object NavBack : SearchAction
  data class NavToImage(val id: NasaId) : SearchAction
  data class EnterSearchTerm(val text: String) : SearchAction
  data object PerformSearch : SearchAction
  data object ConfigureSearch : SearchAction
  data class SelectPage(val pageNumber: Int) : SearchAction
}