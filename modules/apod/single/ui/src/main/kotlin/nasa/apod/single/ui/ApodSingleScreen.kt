package nasa.apod.single.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import nasa.apod.grid.nav.ApodGridNavScreen
import nasa.apod.model.ApodItem
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.single.nav.ApodFullScreenNavScreen
import nasa.apod.single.nav.ApodSingleNavScreen
import nasa.apod.single.vm.ApodSingleViewModel
import nasa.core.ui.getViewModel
import nasa.core.ui.set
import nasa.settings.nav.SettingsNavScreen

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
    val loadCounter = remember { mutableIntStateOf(0) }
    apiKey?.let { key ->
      LaunchedEffect(config, loadCounter.intValue) { viewModel.load(key, config) }
    }

    val settingsScreen = rememberScreen(SettingsNavScreen)

    val displayItem = remember { mutableStateOf<ApodItem?>(null) }
    val immutableDisplayItem = displayItem.value
    if (immutableDisplayItem != null) {
      val displayScreen = rememberScreen(ApodFullScreenNavScreen(immutableDisplayItem))
      navigator.push(displayScreen)
      displayItem.set(null)
    }

    val showDescriptionItem = remember { mutableStateOf<ApodItem?>(null) }
    val immutableDescItem = showDescriptionItem.value
    if (immutableDescItem != null) {
      DescriptionDialog(
        item = immutableDescItem,
        onCancel = { showDescriptionItem.set(null) },
      )
    }

    val loadSpecificDate = remember { mutableStateOf<LocalDate?>(null) }
    val immutableSpecificDate = loadSpecificDate.value
    if (immutableSpecificDate != null) {
      val specific = ApodScreenConfig.Specific(immutableSpecificDate)
      val screen = rememberScreen(ApodSingleNavScreen(specific))
      navigator.replace(screen)
      loadSpecificDate.set(null)
    }

    val gridDate = remember { mutableStateOf<LocalDate?>(null) }
    val immutableGridDate = gridDate.value
    if (immutableGridDate != null) {
      val specific = ApodScreenConfig.Specific(immutableGridDate)
      val screen = rememberScreen(ApodGridNavScreen(specific))
      navigator.push(screen)
      gridDate.set(null)
    }

    val searchDate = remember { mutableStateOf<LocalDate?>(null) }
    val immutableSearchDate = searchDate.value
    if (immutableSearchDate != null) {
      SearchDayDialog(
        initialDate = immutableSearchDate,
        onConfirm = { newDate ->
          loadSpecificDate.set(newDate)
          searchDate.set(null)
        },
        onCancel = { searchDate.set(null) },
      )
    }

    val loadRandom = remember { mutableStateOf(false) }
    if (loadRandom.value) {
      val screen = rememberScreen(ApodSingleNavScreen(ApodScreenConfig.Random()))
      navigator.replace(screen)
      loadRandom.set(false)
    }

    ApodSingleScreenImpl(
      state = state,
      navButtons = navButtons,
      showBackButton = navigator.size > 1,
      onAction = { action ->
        when (action) {
          is ApodSingleAction.NavBack -> navigator.pop()
          is ApodSingleAction.NavSettings -> navigator.push(settingsScreen)
          is ApodSingleAction.RetryLoad -> loadCounter.intValue++
          is ApodSingleAction.OpenVideo -> viewModel.openVideo(action.url)
          ApodSingleAction.RegisterForApiKey -> viewModel.registerForApiKey()
          is ApodSingleAction.ShowDescriptionDialog -> showDescriptionItem.set(action.item)
          is ApodSingleAction.ShowImageFullscreen -> displayItem.set(action.item)
          is ApodSingleAction.LoadNext -> loadSpecificDate.set(action.current + ONE_DAY)
          is ApodSingleAction.LoadPrevious -> loadSpecificDate.set(action.current - ONE_DAY)
          is ApodSingleAction.NavGrid -> gridDate.set(action.current)
          is ApodSingleAction.LoadRandom -> loadRandom.set(true)
          is ApodSingleAction.SearchDate -> searchDate.set(action.current)
        }
      },
    )
  }

  private companion object {
    private val ONE_DAY = DatePeriod(days = 1)
  }
}
