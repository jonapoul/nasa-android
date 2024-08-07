package nasa.gallery.data.repo

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.withContext
import nasa.db.gallery.AlbumDao
import nasa.db.gallery.CenterDao
import nasa.db.gallery.KeywordDao
import nasa.db.gallery.PhotographerDao
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.data.api.GalleryJson
import nasa.gallery.data.api.SearchCollection
import nasa.gallery.data.api.SearchLink
import nasa.gallery.data.api.SearchResponse
import nasa.gallery.model.Album
import nasa.gallery.model.Center
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.Keyword
import nasa.gallery.model.Photographer
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import timber.log.Timber
import javax.inject.Inject

class GallerySearchRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val galleryApi: GalleryApi,
  private val searchPreferences: SearchPreferences,
  private val centerDao: CenterDao,
  private val keywordDao: KeywordDao,
  private val photographerDao: PhotographerDao,
  private val albumDao: AlbumDao,
) {
  suspend fun search(config: FilterConfig, pageNumber: Int?): SearchResult {
    Timber.v("search %s", config)
    if (config == FilterConfig.Empty) return SearchResult.NoFilterSupplied

    val pageSize = searchPreferences.pageSize.get()
    val response = withContext(io) { performSearch(config, pageSize, pageNumber) }

    val searchResponse = if (response.isSuccessful) {
      response.body()
    } else {
      response.errorBody()?.let {
        GalleryJson.decodeFromString(SearchResponse.Failure.serializer(), it.string())
      }
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

  private suspend fun handleSuccess(pageNumber: Int?, pageSize: Int, collection: SearchCollection): SearchResult {
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
      prevPage = collection.pageNumberWithRelation(SearchLink.Relation.Previous),
      nextPage = collection.pageNumberWithRelation(SearchLink.Relation.Next),
    )
  }

  private suspend fun saveMetadata(collection: SearchCollection) {
    val centers = hashSetOf<Center>()
    val keywords = hashSetOf<Keyword>()
    val photographers = hashSetOf<Photographer>()
    val albums = hashSetOf<Album>()
    for (item in collection.items) {
      for (data in item.data) {
        centers.add(data.center)
        data.keywords?.let(keywords::addAll)
        data.photographer?.let(photographers::add)
        data.album?.let(albums::addAll)
      }
    }
    centers.ifIsNotEmpty(centerDao::insertAll)
    keywords.ifIsNotEmpty(keywordDao::insertAll)
    photographers.ifIsNotEmpty(photographerDao::insertAll)
    albums.ifIsNotEmpty(albumDao::insertAll)
    Timber.v("saveMetadata %s %s %s %s", centers, keywords, photographers, albums)
  }

  private suspend fun <T> Set<T>.ifIsNotEmpty(function: suspend (List<T>) -> Unit) {
    if (isNotEmpty()) function(toList())
  }

  private fun SearchCollection.pageNumberWithRelation(relation: SearchLink.Relation) =
    links?.firstOrNull { it.rel == relation }?.url?.parsePageNumber()

  private fun String.parsePageNumber(): Int {
    val url = toHttpUrlOrNull() ?: error("Invalid URL '$this'")
    return url
      .queryParameter(name = "page")
      ?.toIntOrNull()
      ?: error("No integer page query in $url")
  }
}
