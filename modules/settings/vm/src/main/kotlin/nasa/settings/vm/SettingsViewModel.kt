package nasa.settings.vm

import alakazam.android.core.Toaster
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nasa.core.model.FileSize
import nasa.core.model.NASA_API_URL
import nasa.core.model.bytes
import nasa.core.url.UrlOpener
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject internal constructor(
  private val urlOpener: UrlOpener,
  private val imageCache: ImageCache,
  private val databaseClearer: DatabaseClearer,
  private val toaster: Toaster,
) : ViewModel() {
  private val mutableCacheSize = MutableStateFlow(0.bytes)
  val cacheSize: StateFlow<FileSize> = mutableCacheSize.asStateFlow()

  val databaseSize: StateFlow<FileSize> = databaseClearer
    .fileSize
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = 0.bytes)

  init {
    refreshCacheSize()
  }

  fun registerForApiKey() {
    urlOpener.openUrl(NASA_API_URL)
  }

  fun clearCache() {
    viewModelScope.launch {
      val imageClearSuccessful = imageCache.clear()
      val dbClearSuccessful = databaseClearer.clear()
      toaster.coToast(
        when {
          imageClearSuccessful && dbClearSuccessful -> "Successfully cleared cache"
          imageClearSuccessful -> "Database clearing failed - check logs"
          dbClearSuccessful -> "Image cache clearing failed - check logs"
          else -> "Everything failed! Panic!"
        },
      )

      refreshCacheSize()
    }
  }

  fun refreshCacheSize() {
    mutableCacheSize.update { imageCache.calculateSize() }
    databaseClearer.updateFileSize()
  }
}
