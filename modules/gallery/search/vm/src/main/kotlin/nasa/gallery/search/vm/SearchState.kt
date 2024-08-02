package nasa.gallery.search.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface SearchState {
  data object Empty : SearchState

  data object Searching : SearchState

  data class Success(
    val results: ImmutableList<SearchResultItem>,
  ) : SearchState

  data class Failed(
    val reason: String,
  ) : SearchState
}
