package nasa.apod.ui.single

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import nasa.apod.data.repo.FailureResult
import nasa.apod.data.repo.SingleApodRepository
import nasa.apod.data.repo.SingleLoadResult
import nasa.apod.data.repo.reason
import nasa.apod.model.ApodNavButtonsState
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.nav.ApodScreenConfig
import nasa.core.android.UrlOpener
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import nasa.core.model.NASA_API_URL
import javax.inject.Inject

@HiltViewModel
internal class ApodSingleViewModel @Inject constructor(
  private val repository: SingleApodRepository,
  private val urlOpener: UrlOpener,
  apiKeyProvider: ApiKey.Provider,
  calendar: Calendar,
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
  val state: StateFlow<ScreenState> = mutableState.asStateFlow()

  private val mutableApodNavButtonsState = MutableStateFlow(ApodNavButtonsState.BothDisabled)
  val navButtonsState: StateFlow<ApodNavButtonsState> = mutableApodNavButtonsState.asStateFlow()

  init {
    val today = calendar.today()
    viewModelScope.launch {
      mutableState
        .map { it.dateOrNull() }
        .distinctUntilChanged()
        .collect { date ->
          val navButtonState = when (date) {
            null -> ApodNavButtonsState.BothDisabled
            today -> ApodNavButtonsState(enablePrevious = true, enableNext = false)
            EARLIEST_APOD_DATE -> ApodNavButtonsState(enablePrevious = false, enableNext = true)
            else -> ApodNavButtonsState.BothEnabled
          }
          mutableApodNavButtonsState.update { navButtonState }
        }
    }
  }

  val apiKey: StateFlow<ApiKey?> = apiKeyProvider
    .observe()
    .distinctUntilChanged()
    .onEach { if (it == null) mutableState.update { ScreenState.NoApiKey(currentDate()) } }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

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
