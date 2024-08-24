package nasa.apod.ui.grid

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
import nasa.apod.nav.ApodGridNavScreen
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.nav.ApodSingleNavScreen
import nasa.core.ui.getViewModel
import nasa.core.ui.set
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
    val loadCounter = remember { mutableIntStateOf(0) }
    apiKey?.let { key ->
      LaunchedEffect(config, loadCounter.intValue) { viewModel.load(key, config) }
    }

    val loadSpecificDate = remember { mutableStateOf<LocalDate?>(null) }
    val immutableSpecificDate = loadSpecificDate.value
    if (immutableSpecificDate != null) {
      val config = ApodScreenConfig.Specific(immutableSpecificDate)
      val screen = rememberScreen(ApodGridNavScreen(config))
      navigator.replace(screen)
      loadSpecificDate.set(null)
    }

    val searchDate = remember { mutableStateOf<LocalDate?>(null) }
    val immutableSearchDate = searchDate.value
    if (immutableSearchDate != null) {
      SearchMonthDialog(
        initialDate = immutableSearchDate,
        onConfirm = { newDate ->
          searchDate.set(null)
          loadSpecificDate.set(newDate)
        },
        onCancel = { searchDate.set(null) },
      )
    }

    val loadItemDate = remember { mutableStateOf<LocalDate?>(null) }
    val immutableItemDate = loadItemDate.value
    if (immutableItemDate != null) {
      val config = ApodScreenConfig.Specific(immutableItemDate)
      val screen = rememberScreen(ApodSingleNavScreen(config))
      navigator.push(screen)
      loadItemDate.set(null)
    }

    val loadRandom = remember { mutableStateOf(false) }
    if (loadRandom.value) {
      val screen = rememberScreen(ApodGridNavScreen(ApodScreenConfig.Random()))
      navigator.replace(screen)
      loadRandom.set(false)
    }

    val settingsScreen = rememberScreen(SettingsNavScreen)

    ApodGridScreenImpl(
      state = state,
      navButtons = navButtons,
      showBackButton = navigator.size > 1,
      onAction = { action ->
        when (action) {
          is ApodGridAction.NavToItem -> loadItemDate.set(action.item.date)
          is ApodGridAction.SearchMonth -> searchDate.set(action.current)
          is ApodGridAction.RetryLoad -> loadCounter.intValue++
          is ApodGridAction.LoadNext -> loadSpecificDate.set(action.date + ONE_MONTH)
          is ApodGridAction.LoadPrevious -> loadSpecificDate.set(action.date - ONE_MONTH)
          is ApodGridAction.LoadRandom -> loadRandom.set(true)
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
