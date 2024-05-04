package apod.navigation

import apod.core.model.ApodItem
import apod.core.model.GridScreenConfig
import apod.core.model.SingleScreenConfig
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface NavScreens : ScreenProvider {
  data class Apod(val config: SingleScreenConfig) : NavScreens
  data class Grid(val config: GridScreenConfig) : NavScreens
  data class FullScreen(val item: ApodItem) : NavScreens
  data object About : NavScreens
  data object Licenses : NavScreens
  data object Settings : NavScreens
}
