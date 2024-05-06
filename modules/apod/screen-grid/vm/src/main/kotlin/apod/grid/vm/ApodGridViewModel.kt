package apod.grid.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import apod.core.model.Calendar
import apod.core.model.EARLIEST_APOD_DATE
import apod.core.model.NASA_API_URL
import apod.core.model.NavButtonsState
import apod.core.url.UrlOpener
import apod.data.repo.FailureResult
import apod.data.repo.MultipleApodRepository
import apod.data.repo.MultipleLoadResult
import apod.data.repo.reason
import apod.nav.ScreenConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class ApodGridViewModel @Inject internal constructor(
  private val repository: MultipleApodRepository,
  private val urlOpener: UrlOpener,
  apiKeyProvider: ApiKeyProvider,
  calendar: Calendar,
  private val savedState: SavedStateHandle,
) : ViewModel() {
  private var mostRecentDate: LocalDate?
    get() = savedState.get<String>("mostRecentDate")?.let(LocalDate::parse)
    set(value) {
      savedState["mostRecentDate"] = value?.toString()
    }

  private val mutableState = MutableStateFlow<GridScreenState>(GridScreenState.Inactive)
  val state: StateFlow<GridScreenState> = mutableState.asStateFlow()

  private val mutableNavButtonsState = MutableStateFlow(NavButtonsState.BothDisabled)
  val navButtonsState: StateFlow<NavButtonsState> = mutableNavButtonsState.asStateFlow()

  init {
    viewModelScope.launch {
      mutableState
        .map { it.dateOrNull() }
        .distinctUntilChanged()
        .collect { date ->
          val today = calendar.today()
          val navButtonState = when {
            date == null -> NavButtonsState.BothDisabled
            date.matchesMonth(today) -> NavButtonsState(enablePrevButton = true, enableNextButton = false)
            date.matchesMonth(EARLIEST_APOD_DATE) -> NavButtonsState(enablePrevButton = false, enableNextButton = true)
            else -> NavButtonsState.BothEnabled
          }
          mutableNavButtonsState.update { navButtonState }
        }
    }
  }

  val apiKey: StateFlow<ApiKey?> = apiKeyProvider
    .observe()
    .distinctUntilChanged()
    .onEach { if (it == null) mutableState.update { GridScreenState.NoApiKey } }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

  fun registerForApiKey() {
    urlOpener.openUrl(NASA_API_URL)
  }

  fun load(key: ApiKey, config: ScreenConfig) {
    val mostRecent = mostRecentDate
    val configToLoad = if (mostRecent == null) config else ScreenConfig.Specific(mostRecent)

    when (configToLoad) {
      is ScreenConfig.Random -> {
        mutableState.update { GridScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadRandomMonth(key) }
      }

      ScreenConfig.Today -> {
        mutableState.update { GridScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadThisMonth(key) }
      }

      is ScreenConfig.Specific -> {
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

  private fun LocalDate.matchesMonth(other: LocalDate): Boolean {
    return this.year == other.year && this.month == other.month
  }
}
