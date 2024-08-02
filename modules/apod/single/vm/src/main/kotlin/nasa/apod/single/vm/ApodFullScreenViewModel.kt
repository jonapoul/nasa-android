package nasa.apod.single.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nasa.core.http.DownloadProgressStateHolder
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ApodFullScreenViewModel @Inject internal constructor(
  private val stateHolder: DownloadProgressStateHolder,
) : ViewModel() {
  val downloadProgress: StateFlow<Float> = stateHolder
    .state
    .map { state ->
      when {
        state == null -> 0f
        state.done -> 1f
        else -> state.bytesRead.toFloat() / state.contentLength.toFloat()
      }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = 0f)

  override fun onCleared() {
    Timber.v("onCleared")
    stateHolder.reset()
  }
}
