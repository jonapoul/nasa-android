package apod.single.ui

import androidx.compose.runtime.Immutable
import apod.core.model.ApiKey
import apod.core.model.ApodItem
import kotlinx.datetime.LocalDate

@Immutable
internal sealed interface ApodSingleAction {
  data object NavBack : ApodSingleAction
  data object NavAbout : ApodSingleAction
  data object NavSettings : ApodSingleAction
  data class NavGrid(val current: LocalDate) : ApodSingleAction
  data object RegisterForApiKey : ApodSingleAction
  data class ShowImageFullscreen(val item: ApodItem) : ApodSingleAction
  data class OpenVideo(val url: String) : ApodSingleAction
  data class ShowDescriptionDialog(val item: ApodItem) : ApodSingleAction
  data class LoadPrevious(val current: LocalDate) : ApodSingleAction
  data class LoadNext(val current: LocalDate) : ApodSingleAction
  data object LoadRandom : ApodSingleAction
  data class RetryLoad(val key: ApiKey, val date: LocalDate?) : ApodSingleAction
  data class SearchDate(val current: LocalDate?) : ApodSingleAction
}
