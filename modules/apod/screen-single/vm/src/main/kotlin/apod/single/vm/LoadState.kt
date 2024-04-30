package apod.single.vm

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
sealed interface LoadState {
  data object Inactive : LoadState

  data class Loading(
    val date: LocalDate,
  ) : LoadState

  data class Success(
    val item: ApodItem,
  ) : LoadState

  data class Failed(
    val date: LocalDate,
    val message: String,
  ) : LoadState
}
