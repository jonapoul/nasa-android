package nasa.gallery.search.vm

import alakazam.kotlin.core.MainDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nasa.gallery.data.api.SearchItem
import nasa.gallery.data.api.SearchItemLink
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
    val loadingState = if (pageNumber == null) SearchState.Searching else SearchState.LoadingPage(pageNumber)
    mutableSearchState.update { loadingState }

    viewModelScope.launch(main) {
      val searchResult = repository.search(config, pageNumber)
      val searchState = searchResult.toState()
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
    val item = when (data.size) {
      0 -> error("No data in $this")
      1 -> data.first()
      else -> {
        Timber.w("More than one data, grabbing first")
        data.first()
      }
    }
    return SearchResultItem(
      nasaId = item.nasaId,
      collectionUrl = collectionUrl,
      previewUrl = links?.firstOrNull { it.rel == SearchItemLink.Relation.Preview }?.url,
      captionsUrl = links?.firstOrNull { it.rel == SearchItemLink.Relation.Captions }?.url,
      albums = item.album?.toImmutableList()?.ifEmpty { null },
      center = item.center,
      title = item.title,
      keywords = item.keywords,
      location = item.location,
      photographer = item.photographer,
      dateCreated = item.dateCreated,
      mediaType = item.mediaType,
      secondaryCreator = item.secondaryCreator,
      description = item.description,
      description508 = item.description508,
    )
  }
}
