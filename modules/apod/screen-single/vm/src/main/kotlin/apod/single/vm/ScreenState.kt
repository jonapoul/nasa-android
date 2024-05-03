package apod.single.vm

import androidx.compose.runtime.Immutable
import apod.core.model.ApiKey
import apod.core.model.ApodItem
import kotlinx.datetime.LocalDate

@Immutable
sealed interface ScreenState {
  data object Inactive : ScreenState

  data class NoApiKey(
    val date: LocalDate?,
  ) : ScreenState

  data class Loading(
    val date: LocalDate?,
    val key: ApiKey,
  ) : ScreenState

  data class Success(
    val item: ApodItem,
    val key: ApiKey,
  ) : ScreenState

  data class Failed(
    val date: LocalDate?,
    val key: ApiKey,
    val message: String,
  ) : ScreenState
}
