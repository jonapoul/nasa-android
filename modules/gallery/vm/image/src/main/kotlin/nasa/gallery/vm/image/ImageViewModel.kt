package nasa.gallery.vm.image

import alakazam.kotlin.core.MainDispatcher
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nasa.core.http.progress.DownloadProgressStateHolder
import nasa.core.http.progress.toProgress
import nasa.core.model.Percent
import nasa.gallery.data.repo.GalleryImageUrlsRepository
import nasa.gallery.model.NasaId
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject internal constructor(
  private val main: MainDispatcher,
  private val repository: GalleryImageUrlsRepository,
  private val progressStateHolder: DownloadProgressStateHolder,
) : ViewModel() {
  private val mutableImageState = MutableStateFlow<ImageState>(ImageState.Loading)
  val imageState: StateFlow<ImageState> = mutableImageState.asStateFlow()

  val progress: StateFlow<Percent> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val progressState by progressStateHolder.collectAsState()
    progressState?.toProgress() ?: Percent.Zero
  }

  suspend fun load(id: NasaId) {
    val fetchResult = repository.fetchUrls(id)
    Timber.i("fetchResult=$fetchResult")
  }

  fun reload(id: NasaId) {
    viewModelScope.launch(main) { load(id) }
  }

  fun loadMetadata(id: NasaId) {
    Timber.v("loadMetadata $id")
  }
}
