package apod.grid.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.core.ui.getViewModel
import apod.grid.vm.ApodGridAction
import apod.grid.vm.ApodGridViewModel
import apod.nav.NavScreens
import apod.nav.ScreenConfig
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

data class ApodGridScreen(
  val config: ScreenConfig,
) : Screen {
  @Suppress("CyclomaticComplexMethod")
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<ApodGridViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()

    // Load counter increments if the API call failed and the user presses "reload"
    var loadCounter by remember { mutableIntStateOf(0) }
    apiKey?.let { key ->
      LaunchedEffect(config, loadCounter) { viewModel.load(key, config) }
    }

    var loadSpecificDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableSpecificDate = loadSpecificDate
    if (immutableSpecificDate != null) {
      val config = ScreenConfig.Specific(immutableSpecificDate)
      val screen = rememberScreen(NavScreens.Grid(config))
      navigator.replace(screen)
      loadSpecificDate = null
    }

    var searchDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableSearchDate = searchDate
    if (immutableSearchDate != null) {
      SearchMonthDialog(
        initialDate = immutableSearchDate,
        onConfirm = {
          loadSpecificDate = it
          searchDate = null
        },
        onCancel = { searchDate = null },
      )
    }

    var loadItemDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableItemDate = loadItemDate
    if (immutableItemDate != null) {
      val config = ScreenConfig.Specific(immutableItemDate)
      val screen = rememberScreen(NavScreens.Apod(config))
      navigator.push(screen)
      loadItemDate = null
    }

    var loadRandom by remember { mutableStateOf(false) }
    if (loadRandom) {
      val screen = rememberScreen(NavScreens.Grid(ScreenConfig.Random()))
      navigator.replace(screen)
      loadRandom = false
    }

    var showCalendar by remember { mutableStateOf(false) }
    if (showCalendar) {
      // TODO: show calendar
      showCalendar = false
    }

    val settingsScreen = rememberScreen(NavScreens.Settings)

    ApodGridScreenImpl(
      state = state,
      showBackButton = navigator.size > 1,
      onAction = { action ->
        when (action) {
          is ApodGridAction.NavToItem -> {
            loadItemDate = action.item.date
          }

          is ApodGridAction.SearchMonth -> {
            searchDate = action.current
          }

          is ApodGridAction.RetryLoad -> loadCounter++

          is ApodGridAction.LoadNext -> {
            loadSpecificDate = action.date + ONE_MONTH
          }

          is ApodGridAction.LoadPrevious -> {
            loadSpecificDate = action.date - ONE_MONTH
          }

          is ApodGridAction.LoadRandom -> {
            loadRandom = true
          }

          ApodGridAction.ShowCalendar -> {
            showCalendar = true
          }

          ApodGridAction.NavBack -> navigator.pop()

          ApodGridAction.NavSettings -> navigator.push(settingsScreen)

          ApodGridAction.RegisterForApiKey -> viewModel.registerForApiKey()
        }
      },
    )
  }

  private companion object {
    private val ONE_MONTH = DatePeriod(months = 1)
  }
}
