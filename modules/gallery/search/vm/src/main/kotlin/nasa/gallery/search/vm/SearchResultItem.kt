package nasa.gallery.search.vm

import androidx.compose.runtime.Immutable
import nasa.gallery.model.NasaId

@Immutable
data class SearchResultItem(
  val nasaId: NasaId,
)
