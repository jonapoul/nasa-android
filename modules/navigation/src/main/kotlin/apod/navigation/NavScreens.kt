package apod.navigation

import apod.core.model.ApodItem
import cafe.adriel.voyager.core.registry.ScreenProvider
import kotlinx.datetime.LocalDate

sealed interface NavScreens : ScreenProvider {
  data class Home(val date: LocalDate?) : NavScreens
  data class FullScreen(val item: ApodItem) : NavScreens
  data object About : NavScreens
  data object Licenses : NavScreens
  data object Settings : NavScreens
}
