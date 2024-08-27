package nasa.gallery.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.requireMessage
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import nasa.db.gallery.AlbumDao
import nasa.db.gallery.AlbumEntity
import nasa.db.gallery.CenterDao
import nasa.db.gallery.CenterEntity
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.GalleryEntity
import nasa.db.gallery.KeywordDao
import nasa.db.gallery.KeywordEntity
import nasa.db.gallery.PhotographerDao
import nasa.db.gallery.PhotographerEntity
import nasa.gallery.data.api.Collection
import nasa.gallery.data.api.CollectionLink
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.data.api.GalleryJson
import nasa.gallery.data.api.SearchResponse
import nasa.gallery.model.Album
import nasa.gallery.model.Center
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.Keyword
import nasa.gallery.model.Photographer
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class GallerySearchRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val galleryApi: GalleryApi,
  private val searchPreferences: SearchPreferences,
  private val galleryDao: GalleryDao,
  private val centerDao: CenterDao,
  private val keywordDao: KeywordDao,
  private val photographerDao: PhotographerDao,
  private val albumDao: AlbumDao,
) {
  suspend fun search(config: FilterConfig, pageNumber: Int?): SearchResult {
    val pageSize = searchPreferences.pageSize.get()
    return search(config, pageNumber, pageSize)
  }

  suspend fun search(config: FilterConfig, pageNumber: Int?, pageSize: Int): SearchResult {
    Timber.v("search pageNumber=$pageNumber pageSize=$pageSize config=%s", config)
    if (config == FilterConfig.Empty) return SearchResult.NoFilterSupplied

    val response = withContext(io) { performSearch(config, pageSize, pageNumber) }
    val searchResponse = if (response.isSuccessful) response.body() else parseFailure(response.errorBody())

    if (searchResponse is SearchResponse.Success) {
      saveGalleryItems(searchResponse.collection)
    }

    Timber.v("response = %s", response)
    return when (searchResponse) {
      is SearchResponse.Failure -> SearchResult.Failure(searchResponse.reason, config)
      is SearchResponse.Success -> handleSuccess(pageNumber, pageSize, searchResponse.collection)
      null -> SearchResult.Failure(reason = "Null body", config)
    }
  }

  private suspend fun performSearch(config: FilterConfig, pageSize: Int, pageNumber: Int?) = galleryApi.search(
    query = config.query,
    center = config.center,
    description = config.description,
    description508 = null,
    keywords = config.keywords,
    location = config.location,
    mediaTypes = config.mediaTypes,
    nasaId = config.nasaId,
    page = pageNumber,
    pageSize = pageSize,
    photographer = config.photographer,
    secondaryCreator = config.secondaryCreator,
    title = config.title,
    yearStart = config.yearStart,
    yearEnd = config.yearEnd,
  )

  private fun parseFailure(errorBody: ResponseBody?): SearchResponse.Failure {
    errorBody ?: return SearchResponse.Failure(reason = "Null error body")

    val bodyString = errorBody.string()
    return try {
      GalleryJson.decodeFromString(SearchResponse.Failure.serializer(), bodyString)
    } catch (e: SerializationException) {
      Timber.e(e, "Failed deserializing $bodyString")
      SearchResponse.Failure(e.requireMessage())
    }
  }

  private suspend fun handleSuccess(pageNumber: Int?, pageSize: Int, collection: Collection): SearchResult {
    if (collection.metadata.totalHits == 0) {
      Timber.w("Received empty response collection: %s", collection)
      return SearchResult.Empty
    }

    saveMetadata(collection)

    return SearchResult.Success(
      pagedResults = collection.items,
      totalResults = collection.metadata.totalHits,
      maxPerPage = pageSize,
      pageNumber = pageNumber ?: 1,
      prevPage = collection.pageNumberWithRelation(CollectionLink.Relation.Previous),
      nextPage = collection.pageNumberWithRelation(CollectionLink.Relation.Next),
    )
  }

  private suspend fun saveMetadata(collection: Collection) {
    val centers = hashSetOf<Center>()
    val keywords = hashSetOf<Keyword>()
    val photographers = hashSetOf<Photographer>()
    val albums = hashSetOf<Album>()
    for (item in collection.items) {
      for (data in item.data) {
        data.center?.let(centers::add)
        data.keywords?.let(keywords::addAll)
        data.photographer?.let(photographers::add)
        data.album?.let(albums::addAll)
      }
    }
    centers.ifIsNotEmpty(centerDao::insert, ::CenterEntity)
    keywords.ifIsNotEmpty(keywordDao::insert, ::KeywordEntity)
    photographers.ifIsNotEmpty(photographerDao::insert, ::PhotographerEntity)
    albums.ifIsNotEmpty(albumDao::insert, ::AlbumEntity)
    Timber.v("saveMetadata %s %s %s %s", centers, keywords, photographers, albums)
  }

  private suspend fun saveGalleryItems(collection: Collection) {
    val entities = arrayListOf<GalleryEntity>()
    for (collectionItem in collection.items) {
      val collectionUrl = collectionItem.collectionUrl
      for (searchItem in collectionItem.data) {
        entities += GalleryEntity(searchItem.nasaId, collectionUrl, searchItem.mediaType)
      }
    }
    if (entities.isNotEmpty()) {
      galleryDao.insert(entities)
    }
  }

  private suspend fun <T, R> Set<T>.ifIsNotEmpty(function: suspend (List<R>) -> Unit, mapper: (T) -> R) {
    if (isNotEmpty()) function(map { mapper(it) })
  }

  private fun Collection.pageNumberWithRelation(relation: CollectionLink.Relation) =
    links?.firstOrNull { it.rel == relation }?.url?.parsePageNumber()

  private fun String.parsePageNumber(): Int {
    val url = toHttpUrlOrNull() ?: error("Invalid URL '$this'")
    return url
      .queryParameter(name = "page")
      ?.toIntOrNull()
      ?: error("No integer page query in $url")
  }
}
