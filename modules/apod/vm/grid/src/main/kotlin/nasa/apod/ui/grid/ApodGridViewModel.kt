package nasa.apod.ui.grid

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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import nasa.apod.data.repo.FailureResult
import nasa.apod.data.repo.MultipleApodRepository
import nasa.apod.data.repo.MultipleLoadResult
import nasa.apod.data.repo.reason
import nasa.apod.model.ApodNavButtonsState
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.nav.ApodScreenConfig
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import nasa.core.model.NASA_API_URL
import javax.inject.Inject

@HiltViewModel
class ApodGridViewModel @Inject internal constructor(
  private val repository: MultipleApodRepository,
  private val urlOpener: UrlOpener,
  private val apiKeyProvider: ApiKey.Provider,
  private val calendar: Calendar,
  private val savedState: SavedStateHandle,
) : ViewModel() {
  private var mostRecentDate: LocalDate?
    get() = savedState.get<String>("mostRecentDate")?.let(LocalDate::parse)
    set(value) {
      savedState["mostRecentDate"] = value?.toString()
    }

  private val mutableState = MutableStateFlow<GridScreenState>(GridScreenState.Inactive)
  val state: StateFlow<GridScreenState> = viewModelScope.launchMolecule(Immediate) {
    val mutableState by mutableState.collectAsState()
    mutableState
  }

  val navButtonsState: StateFlow<ApodNavButtonsState> = viewModelScope.launchMolecule(Immediate) {
    val screenState by mutableState.collectAsState()
    val date = remember(screenState) { screenState.dateOrNull() }
    val today = remember { calendar.today() }
    when {
      date == null -> ApodNavButtonsState.BothDisabled
      date.matchesMonth(today) -> ApodNavButtonsState(enablePrevious = true, enableNext = false)
      date.matchesMonth(EARLIEST_APOD_DATE) -> ApodNavButtonsState(enablePrevious = false, enableNext = true)
      else -> ApodNavButtonsState.BothEnabled
    }
  }

  val apiKey: StateFlow<ApiKey?> = viewModelScope.launchMolecule(Immediate) {
    val key by apiKeyProvider.observe().collectAsState(initial = null)
    LaunchedEffect(key) {
      if (key == null) mutableState.update { GridScreenState.NoApiKey }
    }
    key
  }

  fun registerForApiKey() {
    urlOpener.openUrl(NASA_API_URL)
  }

  fun load(key: ApiKey, config: ApodScreenConfig) {
    val mostRecent = mostRecentDate
    val configToLoad = if (mostRecent == null) config else ApodScreenConfig.Specific(mostRecent)

    when (configToLoad) {
      is ApodScreenConfig.Random -> {
        mutableState.update { GridScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadRandomMonth(key) }
      }

      ApodScreenConfig.Today -> {
        mutableState.update { GridScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadThisMonth(key) }
      }

      is ApodScreenConfig.Specific -> {
        mostRecentDate = configToLoad.date
        mutableState.update { GridScreenState.Loading(configToLoad.date, key) }
        loadData(key, configToLoad.date) { repository.loadSpecificMonth(key, configToLoad.date) }
      }
    }
  }

  private fun loadData(
    key: ApiKey,
    date: LocalDate?,
    getResult: suspend () -> MultipleLoadResult,
  ) {
    viewModelScope.launch {
      when (val result = getResult()) {
        is MultipleLoadResult.Success -> {
          mutableState.update { GridScreenState.Success(result.items.toImmutableList(), key) }
          mostRecentDate = result.items.first().date
        }

        is FailureResult -> {
          val reason = result.reason()
          mutableState.update { GridScreenState.Failed(date, key, reason) }
        }
      }
    }
  }

  private fun LocalDate.matchesMonth(other: LocalDate): Boolean = year == other.year && month == other.month
}
