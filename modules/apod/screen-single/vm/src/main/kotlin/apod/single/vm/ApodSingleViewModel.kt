package apod.single.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.data.repo.ApodRepository
import apod.data.repo.LoadResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {
  private val mutableState = MutableStateFlow<ScreenState>(ScreenState.Inactive)
  val state: StateFlow<ScreenState> = mutableState.asStateFlow()

  fun load(date: LocalDate?) {
    Timber.d("load $date")
    mutableState.update { ScreenState.Loading(date) }

    viewModelScope.launch {
      when (val result = apodRepository.loadApodItem(date)) {
        is LoadResult.Failure ->
          mutableState.update { ScreenState.Failed(date, result.getReason()) }

        is LoadResult.Success ->
          mutableState.update { ScreenState.Success(result.item) }
      }
    }
  }

  fun loadNext() {
    val currentDate = currentDate()
    if (currentDate != null) {
      load(date = currentDate + ONE_DAY)
    }
  }

  fun loadPrevious() {
    val currentDate = currentDate()
    if (currentDate != null) {
      load(date = currentDate - ONE_DAY)
    }
  }

  private fun currentDate(): LocalDate? = when (val current = mutableState.value) {
    ScreenState.Inactive -> null
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
