package nasa.apod.vm.full

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import nasa.apod.data.repo.LocalDatabaseRepository
import nasa.apod.model.ApodItem
import nasa.core.http.progress.DownloadProgressStateHolder
import nasa.core.http.progress.toProgress
import nasa.core.model.Percent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ApodFullScreenViewModel @Inject internal constructor(
  private val stateHolder: DownloadProgressStateHolder,
  private val repository: LocalDatabaseRepository,
) : ViewModel() {
  val downloadProgress: StateFlow<Percent> = stateHolder
    .state
    .map { it.toProgress() }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = Percent.Zero)

  private val mutableDate = MutableStateFlow<LocalDate?>(null)

  val item: StateFlow<ApodItem?> = mutableDate
    .filterNotNull()
    .map { repository.get(it) }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

  fun loadDate(date: LocalDate) {
    mutableDate.update { date }
  }

  override fun onCleared() {
    Timber.v("onCleared")
    stateHolder.reset()
  }
}
