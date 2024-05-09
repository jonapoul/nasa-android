package nasa.gallery.search.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface SearchAction {
  data object NavBack : SearchAction
  data object RegisterForApiKey : SearchAction
  data object NavSettings : SearchAction
}
