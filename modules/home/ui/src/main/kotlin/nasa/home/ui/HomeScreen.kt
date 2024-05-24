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
import nasa.about.nav.AboutNavScreen
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.single.nav.ApodSingleNavScreen
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.getViewModel
import nasa.gallery.nav.GalleryNavScreen
import nasa.home.vm.HomeViewModel
import nasa.settings.nav.SettingsNavScreen

class HomeScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current

    @Suppress("UNUSED_VARIABLE")
    val viewModel = getViewModel<HomeViewModel>()

    val aboutScreen = rememberScreen(AboutNavScreen)
    var clickedAbout by remember { mutableStateOf(false) }
    if (clickedAbout) {
      navigator.push(aboutScreen)
      clickedAbout = false
    }

    val settingsScreen = rememberScreen(SettingsNavScreen)
    var clickedSettings by remember { mutableStateOf(false) }
    if (clickedSettings) {
      navigator.push(settingsScreen)
      clickedSettings = false
    }

    val apodScreen = rememberScreen(ApodSingleNavScreen(ApodScreenConfig.Today))
    var clickedApodToday by remember { mutableStateOf(false) }
    if (clickedApodToday) {
      navigator.push(apodScreen)
      clickedApodToday = false
    }

    val galleryScreen = rememberScreen(GalleryNavScreen)
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
