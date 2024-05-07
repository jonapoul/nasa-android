package nasa.nav

import cafe.adriel.voyager.core.registry.ScreenProvider
import nasa.apod.model.ApodItem

sealed interface NavScreens : ScreenProvider {
  data object Home : NavScreens
  data class ApodSingle(val config: ApodScreenConfig) : NavScreens
  data class ApodGrid(val config: ApodScreenConfig) : NavScreens
  data class ApodFullScreen(val item: ApodItem) : NavScreens
  data object About : NavScreens
  data object Licenses : NavScreens
  data object Settings : NavScreens
}
