package nasa.apod.single.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nasa.core.http.DownloadProgressStateHolder
import nasa.core.http.toProgress
import nasa.core.model.Percent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ApodFullScreenViewModel @Inject constructor(
  private val stateHolder: DownloadProgressStateHolder,
) : ViewModel() {
  val downloadProgress: StateFlow<Percent> = stateHolder
    .state
    .map { it.toProgress() }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = Percent.Zero)

  override fun onCleared() {
    Timber.v("onCleared")
    stateHolder.reset()
  }
}
