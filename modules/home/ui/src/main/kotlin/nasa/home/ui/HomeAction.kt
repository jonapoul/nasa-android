package nasa.home.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface HomeAction {
  data object NavAbout : HomeAction
  data object NavSettings : HomeAction
  data object NavApodToday : HomeAction
  data object NavGallery : HomeAction
  data object ShowApiUsage : HomeAction
}
