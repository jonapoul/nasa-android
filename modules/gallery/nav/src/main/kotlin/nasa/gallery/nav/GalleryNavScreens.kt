package nasa.gallery.nav

import kotlinx.serialization.Serializable
import nasa.gallery.model.NasaId

@Serializable
data object GallerySearchNavScreen

@Serializable
data class GalleryImageNavScreen(val id: String) {
  val nasaId get() = NasaId(id)
}
