package nasa.gallery.search.vm

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SearchScreenState {
  data object Inactive : SearchScreenState

  data object NoApiKey : SearchScreenState

  data class Loading(
    val placeholder: String,
  ) : SearchScreenState

  data class Success(
    val placeholder: String,
  ) : SearchScreenState

  data class Failed(
    val message: String,
  ) : SearchScreenState
}
