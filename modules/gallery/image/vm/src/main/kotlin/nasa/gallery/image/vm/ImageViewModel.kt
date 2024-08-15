package nasa.gallery.image.vm

import alakazam.kotlin.core.MainDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nasa.core.http.DownloadProgressStateHolder
import nasa.core.http.toProgress
import nasa.core.model.Percent
import nasa.gallery.data.repo.GalleryImageRepository
import nasa.gallery.model.NasaId
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject internal constructor(
  private val main: MainDispatcher,
  private val repository: GalleryImageRepository,
  progressStateHolder: DownloadProgressStateHolder,
) : ViewModel() {
  private val mutableImageState = MutableStateFlow<ImageState>(ImageState.Loading)
  val imageState: StateFlow<ImageState> = mutableImageState.asStateFlow()

  val progress: StateFlow<Percent> = progressStateHolder.state
    .map { it.toProgress() }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = Percent.Zero)

  suspend fun load(id: NasaId) {
    Timber.v("load $id")
  }

  fun reload(id: NasaId) {
    viewModelScope.launch { load(id) }
  }

  fun loadMetadata(id: NasaId) {
    Timber.v("loadMetadata $id")
  }
}
