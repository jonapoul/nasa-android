package apod.single.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.navigation.NavScreens
import apod.single.vm.ApodSingleAction
import apod.single.vm.ApodSingleViewModel
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.LocalDate

data class ApodSingleScreen(
  val date: LocalDate? = null,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow

    val viewModel = getViewModel<ApodSingleViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (date != null) {
      LaunchedEffect(date) { viewModel.load(date) }
    }

    val aboutScreen = rememberScreen(NavScreens.About)
    val settingsScreen = rememberScreen(NavScreens.Settings)

    var clickedAbout by remember { mutableStateOf(false) }
    if (clickedAbout) {
      navigator.push(aboutScreen)
      clickedAbout = false
    }

    var clickedSettings by remember { mutableStateOf(false) }
    if (clickedSettings) {
      navigator.push(settingsScreen)
      clickedSettings = false
    }

    ApodSingleScreenImpl(
      state = state,
      onAction = {
        when (it) {
          ApodSingleAction.NavAbout -> {
            clickedAbout = true
          }

          ApodSingleAction.NavSettings -> {
            clickedSettings = true
          }

          is ApodSingleAction.LoadNext -> {
            viewModel.loadNext()
          }

          is ApodSingleAction.LoadPrevious -> {
            viewModel.loadPrevious()
          }

          is ApodSingleAction.ShowDescriptionDialog -> TODO()

          is ApodSingleAction.ShowImageFullscreen -> TODO()

          is ApodSingleAction.RetryLoad -> TODO()
        }
      },
    )
  }
}
