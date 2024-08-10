package nasa.gallery.model

import kotlinx.serialization.Serializable

sealed interface GalleryUrl : CharSequence {
  val url: String
}

@Serializable
@JvmInline
value class ImageUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}

@Serializable
@JvmInline
value class JsonUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}
