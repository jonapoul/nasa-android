package nasa.gallery.search.vm

import androidx.compose.runtime.Immutable
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.NasaId
import nasa.gallery.model.Year

@Immutable
data class FilterConfig(
  val query: String? = null,
  val center: String? = null,
  val description: String? = null,
  val description508: String? = null,
  val keywords: Keywords? = null,
  val location: String? = null,
  val mediaTypes: MediaTypes? = null,
  val nasaId: NasaId? = null,
  val page: Int? = null,
  val pageSize: Int? = null,
  val photographer: String? = null,
  val secondaryCreator: String? = null,
  val title: String? = null,
  val yearStart: Year? = null,
  val yearEnd: Year? = null,
) {
  companion object {
    val Default = FilterConfig()
  }
}
