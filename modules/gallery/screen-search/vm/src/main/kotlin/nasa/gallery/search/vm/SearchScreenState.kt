package nasa.gallery.search.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface SearchScreenState {
  data object Empty : SearchScreenState

  data object Searching : SearchScreenState

  data class Success(
    val results: ImmutableList<SearchResultItem>,
  ) : SearchScreenState

  data class Failed(
    val reason: String,
  ) : SearchScreenState
}
