package apod.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface NavScreens : ScreenProvider {
  data object Home : NavScreens
  data object About : NavScreens
  data object Licenses : NavScreens
  data object Settings : NavScreens
}
