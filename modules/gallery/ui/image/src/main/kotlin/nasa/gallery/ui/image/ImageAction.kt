package nasa.gallery.ui.image

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ImageAction {
  data object NavBack : ImageAction
  data object RetryLoad : ImageAction
  data object LoadMetadata : ImageAction
}
