package nasa.gallery.search.vm

import alakazam.kotlin.core.MainDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nasa.gallery.data.api.SearchItem
import nasa.gallery.data.repo.GallerySearchRepository
import nasa.gallery.data.repo.SearchResult
import nasa.gallery.model.FilterConfig
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject internal constructor(
  private val main: MainDispatcher,
  private val repository: GallerySearchRepository,
) : ViewModel() {
  private val mutableSearchState = MutableStateFlow<SearchState>(SearchState.NoAction)
  val searchState: StateFlow<SearchState> = mutableSearchState.asStateFlow()

  private val mutableFilterConfig = MutableStateFlow(FilterConfig.Empty)
  val filterConfig: StateFlow<FilterConfig> = mutableFilterConfig.asStateFlow()

  fun retrySearch() {
    performSearch()
  }

  fun performSearch(pageNumber: Int? = null) {
    val config = mutableFilterConfig.value
    Timber.v("performSearch %d %s", pageNumber, config)
    mutableSearchState.update { SearchState.Searching }

    viewModelScope.launch(main) {
      val searchResult = repository.search(config, pageNumber)
      val searchState = searchResult.toState()
      Timber.v("result=%s, state=%s", searchResult, searchState)
      mutableSearchState.update { searchState }
    }
  }

  fun enterSearchTerm(text: String) {
    Timber.v("enterSearchTerm %s", text)
    val newConfig = mutableFilterConfig.value.copy(query = text)
    mutableFilterConfig.update { newConfig }
  }

  fun setFilterConfig(config: FilterConfig) {
    Timber.v("setFilterConfig %s", config)
    mutableFilterConfig.update { config }
  }

  private fun SearchResult.toState() = when (this) {
    SearchResult.Empty -> SearchState.Empty
    SearchResult.NoFilterSupplied -> SearchState.NoFilterConfig
    is SearchResult.Failure -> SearchState.Failed(reason)
    is SearchResult.Success -> SearchState.Success(
      results = pagedResults.map { it.toSearchResultItem() }.toPersistentList(),
      totalResults = totalResults,
      resultsPerPage = maxPerPage,
      prevPageNumber = prevPage,
      pageNumber = pageNumber,
      nextPageNumber = nextPage,
    )
  }

  private fun SearchItem.toSearchResultItem(): SearchResultItem {
    val nasaId = data.firstOrNull()?.nasaId ?: error("No data in $this")
    return SearchResultItem(nasaId = nasaId)
  }
}
