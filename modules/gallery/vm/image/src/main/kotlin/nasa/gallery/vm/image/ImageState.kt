package nasa.gallery.vm.image

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ImageState {
  data object Loading : ImageState

  data class FoundUrl(
    val title: String,
    val imageUrl: String,
    val contentDescription: String,
  ) : ImageState

  data class Failed(
    val reason: String,
  ) : ImageState
}
