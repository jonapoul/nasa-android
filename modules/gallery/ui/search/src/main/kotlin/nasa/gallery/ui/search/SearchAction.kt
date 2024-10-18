package nasa.gallery.ui.search

import androidx.compose.runtime.Immutable
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.NasaId
import nasa.gallery.model.SearchViewConfig

@Immutable
internal sealed interface SearchAction {
  data object NavBack : SearchAction
  data class NavToImage(val id: NasaId) : SearchAction
  data class EnterSearchTerm(val text: String) : SearchAction
  data object PerformSearch : SearchAction
  data class SelectPage(val pageNumber: Int) : SearchAction
  data class SetFilterConfig(val config: FilterConfig) : SearchAction
  data object ToggleExtraConfig : SearchAction
  data object ResetExtraConfig : SearchAction
  data object ShowViewConfigDialog : SearchAction
  data class SetViewConfig(val config: SearchViewConfig) : SearchAction
  data object ResetViewConfig : SearchAction
  data object HideViewTypeDialog : SearchAction
}
