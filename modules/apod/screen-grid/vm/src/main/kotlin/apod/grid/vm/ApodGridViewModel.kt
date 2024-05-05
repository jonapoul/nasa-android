package apod.grid.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import apod.core.model.NASA_API_URL
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
  private val savedState: SavedStateHandle,
) : ViewModel() {
  private var mostRecentDate: LocalDate?
    get() = savedState.get<String>("mostRecentDate")?.let(LocalDate::parse)
    set(value) {
      savedState["mostRecentDate"] = value?.toString()
    }

  private val mutableState = MutableStateFlow<GridScreenState>(GridScreenState.Inactive)
  val state: StateFlow<GridScreenState> = mutableState.asStateFlow()

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
      ScreenConfig.Random -> {
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
}
