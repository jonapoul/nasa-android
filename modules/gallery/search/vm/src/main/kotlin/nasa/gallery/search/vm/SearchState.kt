package nasa.gallery.search.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface SearchState {
  data object NoAction : SearchState

  data object Empty : SearchState

  data object NoFilterConfig : SearchState

  data object Searching : SearchState

  data class Success(
    val results: ImmutableList<SearchResultItem>,
    val totalResults: Int,
    val resultsPerPage: Int,
    val prevPageNumber: Int?,
    val pageNumber: Int,
    val nextPageNumber: Int?,
  ) : SearchState

  data class Failed(
    val reason: String,
  ) : SearchState
}
