package apod.single.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.data.model.ApodItem
import apod.navigation.NavScreens
import apod.single.vm.ApodSingleAction
import apod.single.vm.ApodSingleViewModel
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.LocalDate

// Null date parameter = load the latest item from APOD. Otherwise, attempt to pull that specific date.
data class ApodSingleScreen(
  val date: LocalDate? = null,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow

    val viewModel = getViewModel<ApodSingleViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(date) { viewModel.load(date) }

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

    var showDescriptionItem by remember { mutableStateOf<ApodItem?>(null) }
    val immutableItem = showDescriptionItem
    if (immutableItem != null) {
      DescriptionDialog(
        item = immutableItem,
        onCancel = { showDescriptionItem = null },
      )
    }

    ApodSingleScreenImpl(
      state = state,
      onAction = { action ->
        when (action) {
          is ApodSingleAction.NavAbout -> {
            clickedAbout = true
          }

          is ApodSingleAction.NavSettings -> {
            clickedSettings = true
          }

          is ApodSingleAction.ShowDescriptionDialog -> {
            showDescriptionItem = action.item
          }

          is ApodSingleAction.LoadNext -> viewModel.loadNext()
          is ApodSingleAction.LoadPrevious -> viewModel.loadPrevious()
          is ApodSingleAction.RetryLoad -> viewModel.load(action.date)
          is ApodSingleAction.ShowImageFullscreen -> TODO()
        }
      },
    )
  }
}
