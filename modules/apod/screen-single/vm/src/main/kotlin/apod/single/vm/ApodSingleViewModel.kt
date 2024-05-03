package apod.single.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ApodSingleViewModel @Inject internal constructor(
  private val apodRepository: ApodRepository,
  private val urlOpener: UrlOpener,
  apiKeyProvider: ApiKeyProvider,
) : ViewModel() {
  private val mutableState = MutableStateFlow<ScreenState>(ScreenState.Inactive)
  val state: StateFlow<ScreenState> = mutableState.asStateFlow()

  val apiKey: StateFlow<ApiKey?> = apiKeyProvider
    .observe()
    .distinctUntilChanged()
    .onEach { if (it == null) mutableState.update { ScreenState.NoApiKey(currentDate()) } }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

  fun load(key: ApiKey, date: LocalDate?) {
    Timber.d("load $date")
    mutableState.update { ScreenState.Loading(date, key) }

    viewModelScope.launch {
      when (val result = apodRepository.loadApodItem(key, date)) {
        is LoadResult.Failure ->
          mutableState.update { ScreenState.Failed(date, key, result.getReason()) }

        is LoadResult.Success ->
          mutableState.update { ScreenState.Success(result.item, key) }
      }
    }
  }

  fun loadNext(key: ApiKey) {
    val currentDate = currentDate()
    if (currentDate != null) {
      load(key, date = currentDate + ONE_DAY)
    }
  }

  fun loadPrevious(key: ApiKey) {
    val currentDate = currentDate()
    if (currentDate != null) {
      load(key, date = currentDate - ONE_DAY)
    }
  }

  fun registerForApiKey() {
    urlOpener.openUrl(NASA_API_URL)
  }

  private fun currentDate(): LocalDate? = when (val current = mutableState.value) {
    ScreenState.Inactive -> null
    is ScreenState.NoApiKey -> current.date
    is ScreenState.Failed -> current.date
    is ScreenState.Loading -> current.date
    is ScreenState.Success -> current.item.date
  }

  private fun LoadResult.Failure.getReason(): String = when (this) {
    is LoadResult.Failure.OutOfRange -> message
    is LoadResult.Failure.NoApod -> "No APOD exists for $date"
    is LoadResult.Failure.InvalidAuth -> "Invalid API key - $key"
    is LoadResult.Failure.OtherHttp -> "HTTP code $code - $message"
    is LoadResult.Failure.Json -> "Failed parsing response from server"
    LoadResult.Failure.Network -> "Network problem: does your phone have an internet connection?"
    is LoadResult.Failure.Other -> "Unexpected problem: $message"
  }

  private companion object {
    val ONE_DAY = DatePeriod(days = 1)
  }
}
