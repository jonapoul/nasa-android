package nasa.gallery.vm.search

import alakazam.kotlin.core.MainDispatcher
import alakazam.kotlin.core.StateHolder
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nasa.gallery.data.api.CollectionItem
import nasa.gallery.data.api.CollectionItemLink
import nasa.gallery.data.repo.GallerySearchRepository
import nasa.gallery.data.repo.SearchResult
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject internal constructor(
  private val main: MainDispatcher,
  private val repository: GallerySearchRepository,
) : ViewModel() {
  private val mutableSearchState = MutableStateFlow<SearchState>(SearchState.NoAction)
  private val mutableText = MutableStateFlow(value = "")
  private val mutableYearStart = StateHolder<Year?>(initialState = null)
  private val mutableYearEnd = StateHolder<Year?>(initialState = null)
  private val mutableMediaTypes = StateHolder<MediaTypes?>(initialState = null)

  val searchState: StateFlow<SearchState> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val mutableState by mutableSearchState.collectAsState()
    mutableState
  }

  val filterConfig: StateFlow<FilterConfig> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val text by mutableText.collectAsState()
    val yearStart by mutableYearStart.collectAsState()
    val yearEnd by mutableYearEnd.collectAsState()
    val mediaTypes by mutableMediaTypes.collectAsState()
    FilterConfig(
      query = text,
      yearStart = yearStart,
      yearEnd = yearEnd,
      mediaTypes = mediaTypes,
    )
  }

  fun performSearch(pageNumber: Int? = null) {
    val config = filterConfig.value
    if (config.query.isNullOrBlank()) {
      return
    }

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
    mutableText.update { text }
  }

  fun setFilterConfig(config: FilterConfig) {
    Timber.v("setYearRange %s", config)
    mutableYearStart.update { config.yearStart }
    mutableYearEnd.update { config.yearEnd }
    mutableMediaTypes.update { config.mediaTypes }
  }

  fun resetExtraConfig() {
    mutableYearStart.reset()
    mutableYearEnd.reset()
    mutableMediaTypes.reset()

    with(searchPreferences) {
      yearStart.delete()
      yearEnd.delete()
      mediaTypes.delete()
    }
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

  private fun CollectionItem.toSearchResultItem(): SearchResultItem {
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
      previewUrl = links?.firstOrNull { it.rel == CollectionItemLink.Relation.Preview }?.url,
      captionsUrl = links?.firstOrNull { it.rel == CollectionItemLink.Relation.Captions }?.url,
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
