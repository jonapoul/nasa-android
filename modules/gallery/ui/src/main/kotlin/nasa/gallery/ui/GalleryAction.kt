package nasa.gallery.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface GalleryAction {
  data object NavBack : GalleryAction
  data object RegisterForApiKey : GalleryAction
  data object NavSettings : GalleryAction
}
