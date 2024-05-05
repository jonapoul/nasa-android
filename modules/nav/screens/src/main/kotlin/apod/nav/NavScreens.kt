package apod.nav

import apod.core.model.ApodItem
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface NavScreens : ScreenProvider {
  data class Apod(val config: ScreenConfig) : NavScreens
  data class Grid(val config: ScreenConfig) : NavScreens
  data class FullScreen(val item: ApodItem) : NavScreens
  data object About : NavScreens
  data object Licenses : NavScreens
  data object Settings : NavScreens
}
