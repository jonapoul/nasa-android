package nasa.apod.ui.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import nasa.apod.model.ApodItem
import nasa.apod.nav.ApodFullScreenNavScreen
import nasa.apod.nav.ApodGridSpecificNavScreen
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.nav.ApodSingleRandomNavScreen
import nasa.apod.nav.ApodSingleSpecificNavScreen
import nasa.apod.nav.ApodSingleTodayNavScreen
import nasa.core.ui.set
import nasa.settings.nav.SettingsNavScreen

@Suppress("CyclomaticComplexMethod")
@Composable
fun ApodSingleScreen(
  config: ApodScreenConfig,
  navController: NavController,
  viewModel: ApodSingleViewModel = hiltViewModel(),
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
  val navButtons by viewModel.navButtonsState.collectAsStateWithLifecycle()

  // Load counter increments if the API call failed and the user presses "reload"
  val loadCounter = remember { mutableIntStateOf(0) }
  apiKey?.let { key ->
    LaunchedEffect(config, loadCounter.intValue) { viewModel.load(key, config) }
  }

  val showDescriptionItem = remember { mutableStateOf<ApodItem?>(null) }
  val immutableDescItem = showDescriptionItem.value
  if (immutableDescItem != null) {
    DescriptionDialog(
      item = immutableDescItem,
      onCancel = { showDescriptionItem.set(null) },
    )
  }

  val searchDate = remember { mutableStateOf<LocalDate?>(null) }
  val immutableSearchDate = searchDate.value
  if (immutableSearchDate != null) {
    SearchDayDialog(
      initialDate = immutableSearchDate,
      onConfirm = { newDate ->
        navSpecific(navController, config, newDate)
        searchDate.set(null)
      },
      onCancel = { searchDate.set(null) },
    )
  }

  ApodSingleScreenImpl(
    state = state,
    navButtons = navButtons,
    showBackButton = navController.previousBackStackEntry != null,
    onAction = { action ->
      when (action) {
        is ApodSingleAction.NavBack -> navController.popBackStack()
        is ApodSingleAction.NavSettings -> navController.navigate(route = SettingsNavScreen)
        is ApodSingleAction.RetryLoad -> loadCounter.intValue++
        is ApodSingleAction.OpenVideo -> viewModel.openVideo(action.url)
        ApodSingleAction.RegisterForApiKey -> viewModel.registerForApiKey()
        is ApodSingleAction.ShowDescriptionDialog -> showDescriptionItem.set(action.item)
        is ApodSingleAction.ShowImageFullscreen -> navController.navigate(ApodFullScreenNavScreen(action.date))
        is ApodSingleAction.LoadNext -> navSpecific(navController, config, date = action.current + ONE_DAY)
        is ApodSingleAction.LoadPrevious -> navSpecific(navController, config, date = action.current - ONE_DAY)
        is ApodSingleAction.NavGrid -> navController.navigate(ApodGridSpecificNavScreen(action.current))
        is ApodSingleAction.LoadRandom -> navRandom(navController, config)
        is ApodSingleAction.SearchDate -> searchDate.set(action.current)
      }
    },
  )
}

private fun navSpecific(navController: NavController, config: ApodScreenConfig, date: LocalDate) {
  navController.navigate(route = ApodSingleSpecificNavScreen(date)) {
    popUp(config)
  }
}

private fun navRandom(navController: NavController, config: ApodScreenConfig) {
  navController.navigate(route = ApodSingleRandomNavScreen()) {
    popUp(config)
  }
}

private fun NavOptionsBuilder.popUp(config: ApodScreenConfig) {
  val builder: PopUpToBuilder.() -> Unit = { inclusive = true }
  when (config) {
    is ApodScreenConfig.Random -> popUpTo<ApodSingleRandomNavScreen>(builder)
    is ApodScreenConfig.Specific -> popUpTo<ApodSingleSpecificNavScreen>(builder)
    ApodScreenConfig.Today -> popUpTo<ApodSingleTodayNavScreen>(builder)
  }
}

private val ONE_DAY = DatePeriod(days = 1)
