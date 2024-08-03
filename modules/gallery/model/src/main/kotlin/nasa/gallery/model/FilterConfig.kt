package nasa.gallery.model

import androidx.compose.runtime.Immutable

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
  val photographer: String? = null,
  val secondaryCreator: String? = null,
  val title: String? = null,
  val yearStart: Year? = null,
  val yearEnd: Year? = null,
) {
  companion object {
    val Empty = FilterConfig()
  }
}
