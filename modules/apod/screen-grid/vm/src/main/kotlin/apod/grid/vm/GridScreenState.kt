package apod.grid.vm

import androidx.compose.runtime.Immutable
import apod.core.model.ApiKey
import apod.core.model.ApodItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

@Immutable
sealed interface GridScreenState {
  data object Inactive : GridScreenState

  data object NoApiKey : GridScreenState

  data class Loading(
    val date: LocalDate?,
    val key: ApiKey,
  ) : GridScreenState

  data class Success(
    val items: ImmutableList<ApodItem>,
    val key: ApiKey,
  ) : GridScreenState

  data class Failed(
    val date: LocalDate?,
    val key: ApiKey,
    val message: String,
  ) : GridScreenState
}
