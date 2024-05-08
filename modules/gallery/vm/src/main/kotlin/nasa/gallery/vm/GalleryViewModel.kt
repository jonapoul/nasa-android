package nasa.gallery.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import nasa.core.model.ApiKey
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject internal constructor(
  apiKeyProvider: ApiKey.Provider,
) : ViewModel() {
  private val mutableState = MutableStateFlow<GalleryScreenState>(GalleryScreenState.Inactive)
  val state: StateFlow<GalleryScreenState> = mutableState.asStateFlow()

  val apiKey: StateFlow<ApiKey?> = apiKeyProvider
    .observe()
    .distinctUntilChanged()
    .onEach { if (it == null) mutableState.update { GalleryScreenState.NoApiKey } }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)
}
