package nasa.gallery.search.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface SearchAction {
  data object NavBack : SearchAction
  data class EnterSearchTerm(val text: String) : SearchAction
  data object PerformSearch : SearchAction
  data object ConfigureSearch : SearchAction
  data object RetrySearch : SearchAction
}
