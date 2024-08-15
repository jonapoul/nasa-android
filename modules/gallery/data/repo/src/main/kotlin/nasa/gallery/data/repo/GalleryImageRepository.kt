package nasa.gallery.data.repo

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.withContext
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.ImageUrls
import nasa.gallery.model.JsonUrl
import timber.log.Timber
import javax.inject.Inject

class GalleryImageRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val galleryApi: GalleryApi,
) {
  suspend fun fetchImageCollection(collectionUrl: JsonUrl): FetchImageCollectionResult {
    Timber.v("fetchImageCollection $collectionUrl")
    val response = withContext(io) {
      galleryApi.getCollection(collectionUrl.toString())
    }

    val urlCollection = response.body()
    Timber.v("urlCollection = %s", urlCollection)

    return if (response.isSuccessful && urlCollection != null) {
      FetchImageCollectionResult.Success(
        metadataUrl = urlCollection.filterIsInstance<JsonUrl>().first(),
        imageUrls = ImageUrls(urls = urlCollection.filterIsInstance<ImageUrl>()),
      )
    } else {
      FetchImageCollectionResult.Failure(
        reason = response.errorBody()?.string() ?: "Unknown failure",
      )
    }
  }
}
