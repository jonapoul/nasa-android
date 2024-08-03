package nasa.gallery.search.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject internal constructor() : ViewModel() {
  private val mutableSearchState = MutableStateFlow<SearchState>(SearchState.Empty)
  val searchState: StateFlow<SearchState> = mutableSearchState.asStateFlow()

  private val mutableFilterConfig = MutableStateFlow(DefaultFilterConfig)
  val filterConfig: StateFlow<FilterConfig> = mutableFilterConfig.asStateFlow()

  fun retrySearch() {
    // TBC
  }

  fun performSearch() {
    // TBC
  }

  @Suppress("UNUSED_PARAMETER")
  fun enterSearchTerm(text: String) {
    // TBC
  }

  @Suppress("UNUSED_PARAMETER")
  fun setFilterConfig(config: FilterConfig) {
    // TBC
  }
}