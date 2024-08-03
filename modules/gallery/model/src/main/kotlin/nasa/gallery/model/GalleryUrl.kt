package nasa.gallery.model

import kotlinx.serialization.Serializable

sealed interface GalleryUrl {
  val url: String
}

@Serializable
@JvmInline
value class ImageUrl(override val url: String) : GalleryUrl

@Serializable
@JvmInline
value class JsonUrl(override val url: String) : GalleryUrl
