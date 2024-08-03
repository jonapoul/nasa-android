package nasa.gallery.model

sealed interface GalleryUrl {
  val url: String
}

@JvmInline
value class ImageUrl(override val url: String) : GalleryUrl

@JvmInline
value class JsonUrl(override val url: String) : GalleryUrl
