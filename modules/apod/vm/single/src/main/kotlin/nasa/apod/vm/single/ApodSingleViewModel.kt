package nasa.apod.vm.single

import alakazam.android.core.UrlOpener
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode.Immediate
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import nasa.apod.data.repo.FailureResult
import nasa.apod.data.repo.SingleApodRepository
import nasa.apod.data.repo.SingleLoadResult
import nasa.apod.data.repo.reason
import nasa.apod.model.ApodNavButtonsState
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.nav.ApodScreenConfig
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import nasa.core.model.NASA_API_URL
import javax.inject.Inject

@HiltViewModel
class ApodSingleViewModel @Inject internal constructor(
  private val repository: SingleApodRepository,
  private val urlOpener: UrlOpener,
  private val apiKeyProvider: ApiKey.Provider,
  private val calendar: Calendar,
  private val savedState: SavedStateHandle,
) : ViewModel() {
  // This handles the case where user opens random screen (which has no intrinsic date), taps the image to view in HD,
  // then presses back. By default it would see loadType as random and load another random image, but the user would
  // probably want to go back to the item that was loaded previously. So this stores the most recently-loaded date so
  // we can reload it if needed.
  private var mostRecentDate: LocalDate?
    get() = savedState.get<String>("mostRecentDate")?.let(LocalDate::parse)
    set(value) {
      savedState["mostRecentDate"] = value?.toString()
    }

  private val mutableState = MutableStateFlow<ScreenState>(ScreenState.Inactive)
  val state: StateFlow<ScreenState> = viewModelScope.launchMolecule(Immediate) {
    val screenState by mutableState.collectAsState()
    screenState
  }

  val navButtonsState: StateFlow<ApodNavButtonsState> = viewModelScope.launchMolecule(Immediate) {
    val today = remember { calendar.today() }
    val screenState by mutableState.collectAsState()
    val date = screenState.dateOrNull()
    when (date) {
      null -> ApodNavButtonsState.BothDisabled
      today -> ApodNavButtonsState(enablePrevious = true, enableNext = false)
      EARLIEST_APOD_DATE -> ApodNavButtonsState(enablePrevious = false, enableNext = true)
      else -> ApodNavButtonsState.BothEnabled
    }
  }

  val apiKey: StateFlow<ApiKey?> = viewModelScope.launchMolecule(Immediate) {
    val key by apiKeyProvider.observe().collectAsState(initial = null)
    LaunchedEffect(key) {
      if (key == null) mutableState.update { ScreenState.NoApiKey(currentDate()) }
    }
    key
  }

  fun registerForApiKey() {
    urlOpener.openUrl(NASA_API_URL)
  }

  fun openVideo(url: String) {
    urlOpener.openUrl(url)
  }

  fun load(key: ApiKey, config: ApodScreenConfig) {
    val mostRecent = mostRecentDate
    val configToLoad = if (mostRecent == null) config else ApodScreenConfig.Specific(mostRecent)

    when (configToLoad) {
      is ApodScreenConfig.Random -> {
        mutableState.update { ScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadRandom(key) }
      }

      ApodScreenConfig.Today -> {
        mutableState.update { ScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadToday(key) }
      }

      is ApodScreenConfig.Specific -> {
        mostRecentDate = configToLoad.date
        mutableState.update { ScreenState.Loading(configToLoad.date, key) }
        loadData(key, configToLoad.date) { repository.loadSpecific(key, configToLoad.date) }
      }
    }
  }

  private fun loadData(
    key: ApiKey,
    date: LocalDate?,
    getResult: suspend () -> SingleLoadResult,
  ) {
    viewModelScope.launch {
      when (val result = getResult()) {
        is SingleLoadResult.Success -> {
          mutableState.update { ScreenState.Success(result.item, key) }
          mostRecentDate = result.item.date
        }

        is FailureResult -> {
          val reason = result.reason()
          mutableState.update { ScreenState.Failed(date, key, reason) }
        }
      }
    }
  }

  private fun currentDate(): LocalDate? = mutableState.value.dateOrNull()
}
