package nasa.gallery.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.requireMessage
import kotlinx.coroutines.withContext
import nasa.db.GalleryDao
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.gallery.model.UrlCollection
import javax.inject.Inject

class GalleryImageUrlsRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val galleryApi: GalleryApi,
  private val galleryDao: GalleryDao,
) {
  suspend fun fetchUrls(id: NasaId): FetchUrlsResult {
    val galleryEntity = galleryDao.get(id)
      ?: return FetchUrlsResult.Failure(reason = "No gallery item for $id")

    val mediaType = galleryEntity.mediaType

    // Fetch them from the API
    val response = withContext(io) {
      galleryApi.getCollection(galleryEntity.collectionUrl)
    }

    val fetchedUrls = response.body()
    return if (response.isSuccessful && fetchedUrls != null) {
      try {
        getResult(fetchedUrls, mediaType)
      } catch (e: Exception) {
        FetchUrlsResult.Failure(
          reason = "${e.requireMessage()} - Failed parsing mediaType=$mediaType, fetchedUrls=$fetchedUrls",
        )
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
