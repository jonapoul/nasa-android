package apod.settings.vm

import alakazam.android.core.Toaster
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.NASA_API_URL
import apod.core.url.UrlOpener
import apod.data.repo.DatabaseClearer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    .fileSize()
    .map { it.bytes }
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

  private fun refreshCacheSize() {
    mutableCacheSize.update { imageCache.calculateSize() }
  }
}
