package nasa.gallery.model

import kotlinx.serialization.Serializable

sealed interface GalleryUrl : CharSequence {
  val url: String
}

@Serializable
@JvmInline
value class AudioUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}

@Serializable
@JvmInline
value class ImageUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}

@Serializable
@JvmInline
value class VideoUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}

@Serializable
@JvmInline
value class SubtitleUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}

@Serializable
@JvmInline
value class JsonUrl(override val url: String) : GalleryUrl, CharSequence by url {
  override fun toString() = url
}

data class AudioUrls(private val urls: List<AudioUrl>) : List<AudioUrl> by urls

data class ImageUrls(private val urls: List<ImageUrl>) : List<ImageUrl> by urls {
  fun original(): ImageUrls? = imagesWithTag(tag = "orig")
  fun large(): ImageUrls? = imagesWithTag(tag = "large")
  fun medium(): ImageUrls? = imagesWithTag(tag = "medium")
  fun small(): ImageUrls? = imagesWithTag(tag = "small")
  fun thumb(): ImageUrls? = imagesWithTag(tag = "thumb")

  // E.g. "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~orig.jpg" contains "orig"
  private fun imagesWithTag(tag: String) = urls
    .filter { url -> url.contains("~$tag.", ignoreCase = false) }
    .let { urls -> if (urls.isEmpty()) null else ImageUrls(urls) }
}

data class VideoUrls(private val urls: List<VideoUrl>) : List<VideoUrl> by urls {
  fun original(): VideoUrls? = videosWithTag(tag = "orig")
  fun mobile(): VideoUrls? = videosWithTag(tag = "mobile")
  fun preview(): VideoUrls? = videosWithTag(tag = "preview")

  private fun videosWithTag(tag: String) = urls
    .filter { url -> url.contains("~$tag.", ignoreCase = false) }
    .let { urls -> if (urls.isEmpty()) null else VideoUrls(urls) }
}
