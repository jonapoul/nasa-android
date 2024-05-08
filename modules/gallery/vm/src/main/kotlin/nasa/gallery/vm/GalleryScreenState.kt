package nasa.gallery.vm

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import nasa.core.model.ApiKey

@Immutable
sealed interface GalleryScreenState {
  data object Inactive : GalleryScreenState

  data object NoApiKey : GalleryScreenState

  data class Loading(
    val key: ApiKey,
  ) : GalleryScreenState

  data class Success(
    val key: ApiKey,
  ) : GalleryScreenState

  data class Failed(
    val key: ApiKey,
    val message: String,
  ) : GalleryScreenState
}
