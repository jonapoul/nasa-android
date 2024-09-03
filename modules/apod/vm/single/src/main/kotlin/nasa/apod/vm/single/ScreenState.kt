package nasa.apod.vm.single

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.core.model.ApiKey

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

@Stable
fun ScreenState.dateOrNull(): LocalDate? = when (this) {
  ScreenState.Inactive -> null
  is ScreenState.NoApiKey -> date
  is ScreenState.Failed -> date
  is ScreenState.Loading -> date
  is ScreenState.Success -> item.date
}
