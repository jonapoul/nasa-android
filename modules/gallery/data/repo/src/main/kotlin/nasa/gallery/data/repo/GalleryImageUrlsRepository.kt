package nasa.gallery.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.requireMessage
import kotlinx.coroutines.withContext
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.UrlDao
import nasa.db.gallery.UrlEntity
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.gallery.model.UrlCollection
import timber.log.Timber
import javax.inject.Inject

class GalleryImageUrlsRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val galleryApi: GalleryApi,
  private val galleryDao: GalleryDao,
  private val urlDao: UrlDao,
) {
  suspend fun fetchUrls(id: NasaId): FetchUrlsResult {
    Timber.v("fetchImageCollection $id")

    val galleryEntity = galleryDao.get(id)
      ?: return FetchUrlsResult.Failure(reason = "No gallery item for $id")

    val mediaType = galleryEntity.mediaType

    // First try to grab previously-saved URLs
    val cachedUrls = urlDao.get(id)?.urls
    if (cachedUrls != null) {
      try {
        return getResult(urls = cachedUrls, galleryEntity.mediaType)
      } catch (e: Exception) {
        Timber.e(e, "Failed using $cachedUrls")
      }
    }

    // If none, fetch them from the API
    val response = withContext(io) {
      galleryApi.getCollection(galleryEntity.collectionUrl)
    }

    val fetchedUrls = response.body()
    Timber.v("urlCollection = %s", fetchedUrls)

    return if (response.isSuccessful && fetchedUrls != null) {
      // Save them locally for future reference
      val entity = UrlEntity(galleryId = id, urls = fetchedUrls)
      urlDao.insert(entity)
      try {
        getResult(fetchedUrls, mediaType)
      } catch (e: Exception) {
        Timber.w(e, "Failed parsing to result: mediaType=$mediaType, fetchedUrls=$fetchedUrls")
        FetchUrlsResult.Failure(e.requireMessage())
      }
    } else {
      FetchUrlsResult.Failure(reason = response.errorBody()?.string() ?: "Unknown failure")
    }
  }

  private fun getResult(urls: UrlCollection, type: MediaType): FetchUrlsResult.Success {
    val metadata = urls.metadataUrl()
    val image = urls.imageUrls()
    return when (type) {
      MediaType.Audio -> FetchUrlsResult.Audio(
        metadataUrl = metadata,
        imageUrls = image,
        urls = urls.audioUrls() ?: error("No audio in $urls"),
      )

      MediaType.Image -> FetchUrlsResult.Image(
        metadataUrl = metadata,
        imageUrls = image ?: error("No image in $urls"),
      )

      MediaType.Video -> FetchUrlsResult.Video(
        metadataUrl = metadata,
        imageUrls = image ?: error("No images in $urls"),
        urls = urls.videoUrls() ?: error("No video in $urls"),
      )
    }
  }
}
