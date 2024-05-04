package apod.navigation

import apod.core.model.ApodItem
import apod.core.model.ApodLoadType
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface NavScreens : ScreenProvider {
  data class Apod(val type: ApodLoadType) : NavScreens
  data class FullScreen(val item: ApodItem) : NavScreens
  data object About : NavScreens
  data object Licenses : NavScreens
  data object Settings : NavScreens
}
