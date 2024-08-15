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

data class ImageUrls(private val urls: List<ImageUrl>) : List<ImageUrl> by urls {
  fun original(): ImageUrl? = imageWithTag(tag = "orig")
  fun large(): ImageUrl? = imageWithTag(tag = "large")
  fun medium(): ImageUrl? = imageWithTag(tag = "medium")
  fun small(): ImageUrl? = imageWithTag(tag = "small")
  fun thumb(): ImageUrl? = imageWithTag(tag = "thumb")

  // E.g. "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~orig.jpg" contains "orig"
  private fun imageWithTag(tag: String) = urls.firstOrNull { it.contains("~$tag.", ignoreCase = false) }
}
