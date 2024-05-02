package apod.single.vm

import androidx.compose.runtime.Immutable
import apod.core.model.ApodItem
import kotlinx.datetime.LocalDate

@Immutable
sealed interface ScreenState {
  data object Inactive : ScreenState

  data class Loading(
    val date: LocalDate,
  ) : ScreenState

  data class Success(
    val item: ApodItem,
  ) : ScreenState

  data class Failed(
    val date: LocalDate,
    val message: String,
  ) : ScreenState
}
