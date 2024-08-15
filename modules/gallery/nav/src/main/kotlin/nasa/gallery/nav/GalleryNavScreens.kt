package nasa.gallery.nav

import cafe.adriel.voyager.core.registry.ScreenProvider
import nasa.gallery.model.NasaId

data object GallerySearchNavScreen : ScreenProvider

data class GalleryImageNavScreen(
  val id: NasaId,
  val title: String,
) : ScreenProvider
