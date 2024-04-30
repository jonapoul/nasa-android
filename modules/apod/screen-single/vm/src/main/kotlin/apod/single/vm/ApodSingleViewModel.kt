package apod.single.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ApodSingleViewModel @Inject constructor() : ViewModel() {
  private val mutableState = MutableStateFlow<LoadState>(LoadState.Inactive)
  val state: StateFlow<LoadState> = mutableState.asStateFlow()

  fun load(date: LocalDate) {
    Timber.d("load $date")
    mutableState.update { LoadState.Loading(date) }
    // TODO: actually load it
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
    LoadState.Inactive -> null
    is LoadState.Failed -> current.date
    is LoadState.Loading -> current.date
    is LoadState.Success -> current.item.date
  }

  private companion object {
    val ONE_DAY = DatePeriod(days = 1)
  }
}
