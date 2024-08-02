package nasa.apod.grid.ui

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
import nasa.apod.grid.nav.ApodGridNavScreen
import nasa.apod.grid.vm.ApodGridViewModel
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.single.nav.ApodSingleNavScreen
import nasa.core.ui.getViewModel
import nasa.settings.nav.SettingsNavScreen

data class ApodGridScreen(
  val config: ApodScreenConfig,
) : Screen {
  @Suppress("CyclomaticComplexMethod")
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<ApodGridViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
    val navButtons by viewModel.navButtonsState.collectAsStateWithLifecycle()

    // Load counter increments if the API call failed and the user presses "reload"
    var loadCounter by remember { mutableIntStateOf(0) }
    apiKey?.let { key ->
      LaunchedEffect(config, loadCounter) { viewModel.load(key, config) }
    }

    var loadSpecificDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableSpecificDate = loadSpecificDate
    if (immutableSpecificDate != null) {
      val config = ApodScreenConfig.Specific(immutableSpecificDate)
      val screen = rememberScreen(ApodGridNavScreen(config))
      navigator.replace(screen)
      loadSpecificDate = null
    }

    var searchDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableSearchDate = searchDate
    if (immutableSearchDate != null) {
      SearchMonthDialog(
        initialDate = immutableSearchDate,
        onConfirm = {
          searchDate = null
          loadSpecificDate = it
        },
        onCancel = { searchDate = null },
      )
    }

    var loadItemDate by remember { mutableStateOf<LocalDate?>(null) }
    val immutableItemDate = loadItemDate
    if (immutableItemDate != null) {
      val config = ApodScreenConfig.Specific(immutableItemDate)
      val screen = rememberScreen(ApodSingleNavScreen(config))
      navigator.push(screen)
      loadItemDate = null
    }

    var loadRandom by remember { mutableStateOf(false) }
    if (loadRandom) {
      val screen = rememberScreen(ApodGridNavScreen(ApodScreenConfig.Random()))
      navigator.replace(screen)
      loadRandom = false
    }

    val settingsScreen = rememberScreen(SettingsNavScreen)

    ApodGridScreenImpl(
      state = state,
      navButtons = navButtons,
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
