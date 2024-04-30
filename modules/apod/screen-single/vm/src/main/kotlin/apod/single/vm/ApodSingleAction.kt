package apod.single.vm

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
sealed interface ApodSingleAction {
  data object NavAbout : ApodSingleAction
  data object NavSettings : ApodSingleAction
  data class ShowImageFullscreen(val item: ApodItem) : ApodSingleAction
  data class ShowDescriptionDialog(val item: ApodItem) : ApodSingleAction
  data class LoadPrevious(val current: LocalDate) : ApodSingleAction
  data class LoadNext(val current: LocalDate) : ApodSingleAction
}
