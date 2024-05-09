package nasa.apod.single.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import nasa.apod.model.ApodItem
import nasa.apod.single.vm.ApodSingleViewModel
import nasa.core.ui.getViewModel
import nasa.nav.ApodScreenConfig
import nasa.nav.NavScreens

data class ApodSingleScreen(
  val config: ApodScreenConfig,
) : Screen {
  @Suppress("CyclomaticComplexMethod")
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<ApodSingleViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
    val navButtons by viewModel.navButtonsState.collectAsStateWithLifecycle()

    // Load counter increments if the API call failed and the user presses "reload"
    var loadCounter by remember { mutableIntStateOf(0) }
    apiKey?.let { key ->
      LaunchedEffect(config, loadCounter) { viewModel.load(key, config) }
    }

    val settingsScreen = rememberScreen(NavScreens.Settings)

    var displayItem by remember { mutableStateOf<ApodItem?>(null) }
    val immutableDisplayItem = displayItem
    if (immutableDisplayItem != null) {
      val displayScreen = rememberScreen(NavScreens.ApodFullScreen(immutableDisplayItem))
      navigator.push(displayScreen)
      displayItem = null
    }

    var showDescriptionItem by remember { mutableStateOf<ApodItem?>(null) }
    val immutableDescItem = showDescriptionItem
    if (immutableDescItem != null) {
      DescriptionDialog(
        item = immutableDescItem,
        onCancel = { showDescriptionItem = null },
      )
    }

    var loadSpecificDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableSpecificDate = loadSpecificDate
    if (immutableSpecificDate != null) {
      val specific = ApodScreenConfig.Specific(immutableSpecificDate)
      val screen = rememberScreen(NavScreens.ApodSingle(specific))
      navigator.replace(screen)
      loadSpecificDate = null
    }

    var gridDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableGridDate = gridDate
    if (immutableGridDate != null) {
      val specific = ApodScreenConfig.Specific(immutableGridDate)
      val screen = rememberScreen(NavScreens.ApodGrid(specific))
      navigator.push(screen)
      gridDate = null
    }

    var searchDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableSearchDate = searchDate
    if (immutableSearchDate != null) {
      SearchDayDialog(
        initialDate = immutableSearchDate,
        onConfirm = {
          loadSpecificDate = it
          searchDate = null
        },
        onCancel = { searchDate = null },
      )
    }

    var loadRandom by remember { mutableStateOf(false) }
    if (loadRandom) {
      val screen = rememberScreen(NavScreens.ApodSingle(ApodScreenConfig.Random()))
      navigator.replace(screen)
      loadRandom = false
    }

    ApodSingleScreenImpl(
      state = state,
      navButtons = navButtons,
      showBackButton = navigator.size > 1,
      onAction = { action ->
        when (action) {
          is ApodSingleAction.NavBack -> navigator.pop()
          is ApodSingleAction.NavSettings -> navigator.push(settingsScreen)
          is ApodSingleAction.RetryLoad -> loadCounter++
          is ApodSingleAction.OpenVideo -> viewModel.openVideo(action.url)
          ApodSingleAction.RegisterForApiKey -> viewModel.registerForApiKey()

          is ApodSingleAction.ShowDescriptionDialog -> {
            showDescriptionItem = action.item
          }

          is ApodSingleAction.ShowImageFullscreen -> {
            displayItem = action.item
          }

          is ApodSingleAction.LoadNext -> {
            loadSpecificDate = action.current + ONE_DAY
          }

          is ApodSingleAction.LoadPrevious -> {
            loadSpecificDate = action.current - ONE_DAY
          }

          is ApodSingleAction.NavGrid -> {
            gridDate = action.current
          }

          is ApodSingleAction.LoadRandom -> {
            loadRandom = true
          }

          is ApodSingleAction.SearchDate -> {
            searchDate = action.current
          }
        }
      },
    )
  }

  private companion object {
    private val ONE_DAY = DatePeriod(days = 1)
  }
}
