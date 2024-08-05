package nasa.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.about.nav.AboutNavScreen
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.single.nav.ApodSingleNavScreen
import nasa.core.http.ApiUsageState
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.getViewModel
import nasa.core.ui.set
import nasa.gallery.nav.GalleryNavScreen
import nasa.home.vm.HomeViewModel
import nasa.settings.nav.SettingsNavScreen

class HomeScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current

    val viewModel = getViewModel<HomeViewModel>()

    val aboutScreen = rememberScreen(AboutNavScreen)
    val clickedAbout = remember { mutableStateOf(false) }
    if (clickedAbout.value) {
      navigator.push(aboutScreen)
      clickedAbout.set(false)
    }

    val settingsScreen = rememberScreen(SettingsNavScreen)
    val clickedSettings = remember { mutableStateOf(false) }
    if (clickedSettings.value) {
      navigator.push(settingsScreen)
      clickedSettings.set(false)
    }

    val apodScreen = rememberScreen(ApodSingleNavScreen(ApodScreenConfig.Today))
    val clickedApodToday = remember { mutableStateOf(false) }
    if (clickedApodToday.value) {
      navigator.push(apodScreen)
      clickedApodToday.set(false)
    }

    val galleryScreen = rememberScreen(GalleryNavScreen)
    val clickedGallery = remember { mutableStateOf(false) }
    if (clickedGallery.value) {
      navigator.push(galleryScreen)
      clickedGallery.set(false)
    }

    val showApiUsageDialog = remember { mutableStateOf(false) }
    if (showApiUsageDialog.value) {
      val state by viewModel.apiUsage().collectAsState(initial = ApiUsageState.RealKeyNoUsage)
      ApiUsageDialog(
        state = state,
        theme = theme,
        onDismiss = { showApiUsageDialog.set(false) },
        onClickRegister = { viewModel.registerForApiKey() },
      )
    }

    HomeScreenImpl(
      theme = theme,
      onAction = { action ->
        when (action) {
          HomeAction.NavAbout -> clickedAbout.set(true)
          HomeAction.NavSettings -> clickedSettings.set(true)
          HomeAction.NavApodToday -> clickedApodToday.set(true)
          HomeAction.NavGallery -> clickedGallery.set(true)
          HomeAction.ShowApiUsage -> showApiUsageDialog.set(true)
        }
      },
    )
  }
}
