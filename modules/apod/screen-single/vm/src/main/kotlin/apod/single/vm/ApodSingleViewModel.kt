package apod.single.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import apod.core.model.NASA_API_URL
import apod.core.url.UrlOpener
import apod.data.repo.FailureResult
import apod.data.repo.SingleApodRepository
import apod.data.repo.SingleLoadResult
import apod.data.repo.reason
import apod.nav.ScreenConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class ApodSingleViewModel @Inject internal constructor(
  private val repository: SingleApodRepository,
  private val urlOpener: UrlOpener,
  apiKeyProvider: ApiKeyProvider,
  private val savedState: SavedStateHandle,
) : ViewModel() {
  // This handles the case where user opens random screen (which has no intrinsic date), taps the image to view in HD,
  // then presses back. By default it would see loadType as random and load another random image, but the user would
  // probably want to go back to the item that was loaded previously.
  private var mostRecentDate: LocalDate?
    get() = savedState.get<String>("mostRecentDate")?.let(LocalDate::parse)
    set(value) {
      savedState["mostRecentDate"] = value?.toString()
    }

  private val mutableState = MutableStateFlow<ScreenState>(ScreenState.Inactive)
  val state: StateFlow<ScreenState> = mutableState.asStateFlow()

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

  fun load(key: ApiKey, config: ScreenConfig) {
    val mostRecent = mostRecentDate
    val configToLoad = if (mostRecent == null) config else ScreenConfig.Specific(mostRecent)

    when (configToLoad) {
      is ScreenConfig.Random -> {
        mutableState.update { ScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadRandom(key) }
      }

      ScreenConfig.Today -> {
        mutableState.update { ScreenState.Loading(date = null, key) }
        loadData(key, date = null) { repository.loadToday(key) }
      }

      is ScreenConfig.Specific -> {
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
