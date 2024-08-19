package nasa.db.gallery

import nasa.gallery.model.JsonUrl
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId

interface GalleryEntity {
  val id: NasaId
  val collectionUrl: JsonUrl
  val mediaType: MediaType

  fun interface Factory {
    operator fun invoke(
      id: NasaId,
      collectionUrl: JsonUrl,
      type: MediaType,
    ): GalleryEntity
  }
}
