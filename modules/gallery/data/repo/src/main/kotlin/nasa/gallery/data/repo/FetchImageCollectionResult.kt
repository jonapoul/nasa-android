package nasa.gallery.data.repo

import nasa.gallery.model.ImageUrls
import nasa.gallery.model.JsonUrl

sealed interface FetchImageCollectionResult {
  data class Failure(
    val reason: String,
  ) : FetchImageCollectionResult

  data class Success(
    val metadataUrl: JsonUrl,
    val imageUrls: ImageUrls,
  ) : FetchImageCollectionResult
}
