package nasa.gallery.ui.search

import androidx.compose.runtime.Immutable
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.NasaId
import nasa.gallery.model.Year

@Immutable
internal sealed interface SearchAction {
  data object NavBack : SearchAction
  data class NavToImage(val id: NasaId) : SearchAction
  data class EnterSearchTerm(val text: String) : SearchAction
  data object PerformSearch : SearchAction
  data class SelectPage(val pageNumber: Int) : SearchAction
  data class SetYearRange(val min: Year, val max: Year) : SearchAction
  data class SetMediaTypes(val types: MediaTypes) : SearchAction
}
