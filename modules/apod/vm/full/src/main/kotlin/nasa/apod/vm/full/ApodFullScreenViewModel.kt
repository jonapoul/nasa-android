package nasa.apod.vm.full

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode.Immediate
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
  private val progressStateHolder: DownloadProgressStateHolder,
  private val repository: LocalDatabaseRepository,
) : ViewModel() {
  val downloadProgress: StateFlow<Percent> = viewModelScope.launchMolecule(Immediate) {
    val downloadState by progressStateHolder.state.collectAsState()
    downloadState.toProgress()
  }

  private val mutableDate = MutableStateFlow<LocalDate?>(null)

  val item: StateFlow<ApodItem?> = viewModelScope.launchMolecule(Immediate) {
    val date by mutableDate.collectAsState()
    val constDate = date
    var item by remember { mutableStateOf<ApodItem?>(null) }

    LaunchedEffect(constDate) {
      if (constDate != null) {
        item = repository.get(constDate)
      }
    }

    item
  }

  fun loadDate(date: LocalDate) {
    mutableDate.update { date }
  }

  override fun onCleared() {
    Timber.v("onCleared")
    progressStateHolder.reset()
  }
}
