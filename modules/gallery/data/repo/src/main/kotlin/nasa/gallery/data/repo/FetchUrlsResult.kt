package nasa.gallery.data.repo

import nasa.gallery.model.AudioUrls
import nasa.gallery.model.GalleryUrl
import nasa.gallery.model.ImageUrls
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.VideoUrls

sealed interface FetchUrlsResult {
  data class Failure(
    val reason: String,
  ) : FetchUrlsResult

  sealed interface Success : FetchUrlsResult {
    val metadataUrl: JsonUrl
    val imageUrls: ImageUrls?
    val urls: List<GalleryUrl>
  }

  data class Audio(
    override val metadataUrl: JsonUrl,
    override val imageUrls: ImageUrls?,
    override val urls: AudioUrls,
  ) : Success

  data class Image(
    override val metadataUrl: JsonUrl,
    override val imageUrls: ImageUrls,
  ) : Success {
    override val urls = imageUrls
  }

  data class Video(
    override val metadataUrl: JsonUrl,
    override val imageUrls: ImageUrls,
    override val urls: VideoUrls,
  ) : Success
}
