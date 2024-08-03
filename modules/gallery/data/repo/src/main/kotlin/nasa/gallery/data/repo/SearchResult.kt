package nasa.gallery.data.repo

import nasa.gallery.data.api.SearchItem
import nasa.gallery.model.FilterConfig

sealed interface SearchResult {
  data object Empty : SearchResult

  data object NoFilterSupplied : SearchResult

  data class Failure(
    val reason: String,
    val config: FilterConfig,
  ) : SearchResult

  data class Success(
    val pagedResults: List<SearchItem>,
    val totalResults: Int,
    val maxPerPage: Int,
    val pageNumber: Int,
    val prevPage: Int?,
    val nextPage: Int?,
  ) : SearchResult
}
