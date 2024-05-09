package nasa.gallery.search.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject internal constructor() : ViewModel() {
  private val mutableState = MutableStateFlow<SearchScreenState>(SearchScreenState.Inactive)
  val state: StateFlow<SearchScreenState> = mutableState.asStateFlow()
}
