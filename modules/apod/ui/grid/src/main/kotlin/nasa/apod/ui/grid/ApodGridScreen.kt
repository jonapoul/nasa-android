package nasa.apod.ui.grid

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
import nasa.apod.nav.ApodGridRandomNavScreen
import nasa.apod.nav.ApodGridSpecificNavScreen
import nasa.apod.nav.ApodGridTodayNavScreen
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.nav.ApodSingleSpecificNavScreen
import nasa.core.ui.set
import nasa.settings.nav.SettingsNavScreen

@Composable
fun ApodGridScreen(
  config: ApodScreenConfig,
  navController: NavController,
  viewModel: ApodGridViewModel = hiltViewModel(),
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
  val navButtons by viewModel.navButtonsState.collectAsStateWithLifecycle()

  // Load counter increments if the API call failed and the user presses "reload"
  val loadCounter = remember { mutableIntStateOf(0) }
  apiKey?.let { key ->
    LaunchedEffect(config, loadCounter.intValue) { viewModel.load(key, config) }
  }

  val searchDate = remember { mutableStateOf<LocalDate?>(null) }
  val immutableSearchDate = searchDate.value
  if (immutableSearchDate != null) {
    SearchMonthDialog(
      initialDate = immutableSearchDate,
      onConfirm = { newDate ->
        searchDate.set(null)
        navSpecific(navController, config, newDate)
      },
      onCancel = { searchDate.set(null) },
    )
  }

  ApodGridScreenImpl(
    state = state,
    navButtons = navButtons,
    showBackButton = navController.previousBackStackEntry != null,
    onAction = { action ->
      when (action) {
        is ApodGridAction.NavToItem -> navController.navigate(ApodSingleSpecificNavScreen(action.item.date))
        is ApodGridAction.SearchMonth -> searchDate.set(action.current)
        is ApodGridAction.RetryLoad -> loadCounter.intValue++
        is ApodGridAction.LoadNext -> navSpecific(navController, config, date = action.date + ONE_MONTH)
        is ApodGridAction.LoadPrevious -> navSpecific(navController, config, date = action.date - ONE_MONTH)
        is ApodGridAction.LoadRandom -> navRandom(navController, config)
        ApodGridAction.NavBack -> navController.popBackStack()
        ApodGridAction.NavSettings -> navController.navigate(SettingsNavScreen)
        ApodGridAction.RegisterForApiKey -> viewModel.registerForApiKey()
      }
    },
  )
}

private fun navSpecific(navController: NavController, config: ApodScreenConfig, date: LocalDate) {
  navController.navigate(route = ApodGridSpecificNavScreen(date)) {
    popUp(config)
  }
}

private fun navRandom(navController: NavController, config: ApodScreenConfig) {
  navController.navigate(route = ApodGridRandomNavScreen()) {
    popUp(config)
  }
}

private fun NavOptionsBuilder.popUp(config: ApodScreenConfig) {
  val builder: PopUpToBuilder.() -> Unit = { inclusive = true }
  when (config) {
    is ApodScreenConfig.Random -> popUpTo<ApodGridRandomNavScreen>(builder)
    is ApodScreenConfig.Specific -> popUpTo<ApodGridSpecificNavScreen>(builder)
    ApodScreenConfig.Today -> popUpTo<ApodGridTodayNavScreen>(builder)
  }
}

private val ONE_MONTH = DatePeriod(months = 1)
