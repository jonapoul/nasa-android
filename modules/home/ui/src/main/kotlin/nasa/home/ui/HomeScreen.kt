package nasa.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.getViewModel
import nasa.home.vm.HomeViewModel
import nasa.nav.ApodScreenConfig
import nasa.nav.NavScreens

class HomeScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current

    @Suppress("UNUSED_VARIABLE")
    val viewModel = getViewModel<HomeViewModel>()

    val aboutScreen = rememberScreen(NavScreens.About)
    var clickedAbout by remember { mutableStateOf(false) }
    if (clickedAbout) {
      navigator.push(aboutScreen)
      clickedAbout = false
    }

    val settingsScreen = rememberScreen(NavScreens.Settings)
    var clickedSettings by remember { mutableStateOf(false) }
    if (clickedSettings) {
      navigator.push(settingsScreen)
      clickedSettings = false
    }

    val apodScreen = rememberScreen(NavScreens.ApodSingle(ApodScreenConfig.Today))
    var clickedApodToday by remember { mutableStateOf(false) }
    if (clickedApodToday) {
      navigator.push(apodScreen)
      clickedApodToday = false
    }

    val galleryScreen = rememberScreen(NavScreens.Gallery)
    var clickedGallery by remember { mutableStateOf(false) }
    if (clickedGallery) {
      navigator.push(galleryScreen)
      clickedGallery = false
    }

    HomeScreenImpl(
      theme = theme,
      onAction = { action ->
        when (action) {
          HomeAction.NavAbout -> {
            clickedAbout = true
          }

          HomeAction.NavSettings -> {
            clickedSettings = true
          }

          HomeAction.NavApodToday -> {
            clickedApodToday = true
          }

          HomeAction.NavGallery -> {
            clickedGallery = true
          }
        }
      },
    )
  }
}
