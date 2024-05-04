package apod.single.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import apod.core.model.ApodLoadType
import apod.core.model.NASA_API_URL
import apod.core.url.UrlOpener
import apod.data.repo.ApodRepository
import apod.data.repo.LoadResult
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ApodSingleViewModel @Inject internal constructor(
  private val apodRepository: ApodRepository,
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

  fun load(key: ApiKey, type: ApodLoadType) {
    val mostRecent = mostRecentDate
    val typeToLoad = if (mostRecent == null) type else ApodLoadType.Specific(mostRecent)
    Timber.d("load $type mostRecent=$mostRecent typeToLoad=$typeToLoad")

    when (typeToLoad) {
      ApodLoadType.Random -> {
        mutableState.update { ScreenState.Loading(date = null, key) }
        loadData(key, date = null) { apodRepository.loadRandom(key) }
      }

      ApodLoadType.Today -> {
        mutableState.update { ScreenState.Loading(date = null, key) }
        loadData(key, date = null) { apodRepository.loadToday(key) }
      }

      is ApodLoadType.Specific -> {
        mostRecentDate = typeToLoad.date
        mutableState.update { ScreenState.Loading(typeToLoad.date, key) }
        loadData(key, typeToLoad.date) { apodRepository.loadSpecific(key, typeToLoad.date) }
      }
    }
  }

  private fun loadData(
    key: ApiKey,
    date: LocalDate?,
    getResult: suspend () -> LoadResult,
  ) {
    viewModelScope.launch {
      when (val result = getResult()) {
        is LoadResult.Success -> {
          mutableState.update { ScreenState.Success(result.item, key) }
          mostRecentDate = result.item.date
        }

        is LoadResult.Failure -> {
          val reason = when (result) {
            is LoadResult.Failure.OutOfRange -> result.message
            is LoadResult.Failure.NoApod -> "No APOD exists for $date"
            is LoadResult.Failure.InvalidAuth -> "Invalid API key - $key"
            is LoadResult.Failure.OtherHttp -> "HTTP code ${result.code} - ${result.message}"
            is LoadResult.Failure.Json -> "Failed parsing response from server"
            LoadResult.Failure.Network -> "Network problem: does your phone have an internet connection?"
            is LoadResult.Failure.Other -> "Unexpected problem: ${result.message}"
          }
          mutableState.update { ScreenState.Failed(date, key, reason) }
        }
      }
    }
  }

  private fun currentDate(): LocalDate? = when (val current = mutableState.value) {
    ScreenState.Inactive -> null
    is ScreenState.NoApiKey -> current.date
    is ScreenState.Failed -> current.date
    is ScreenState.Loading -> current.date
    is ScreenState.Success -> current.item.date
  }
}
