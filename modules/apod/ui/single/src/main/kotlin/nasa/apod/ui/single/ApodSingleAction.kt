package nasa.apod.ui.single

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.core.model.ApiKey

@Immutable
internal sealed interface ApodSingleAction {
  data object NavBack : ApodSingleAction
  data object NavSettings : ApodSingleAction
  data class NavGrid(val current: LocalDate) : ApodSingleAction
  data object RegisterForApiKey : ApodSingleAction
  data class ShowImageFullscreen(val date: LocalDate) : ApodSingleAction
  data class OpenVideo(val url: String) : ApodSingleAction
  data class ShowDescriptionDialog(val item: ApodItem) : ApodSingleAction
  data class LoadPrevious(val current: LocalDate) : ApodSingleAction
  data class LoadNext(val current: LocalDate) : ApodSingleAction
  data object LoadRandom : ApodSingleAction
  data class RetryLoad(val key: ApiKey, val date: LocalDate?) : ApodSingleAction
  data class SearchDate(val current: LocalDate?) : ApodSingleAction
}
