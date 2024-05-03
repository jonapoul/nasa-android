package apod.settings.vm

import androidx.lifecycle.ViewModel
import apod.core.model.NASA_API_URL
import apod.core.url.UrlOpener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject internal constructor(
  private val urlOpener: UrlOpener,
) : ViewModel() {
  fun registerForApiKey() {
    urlOpener.openUrl(NASA_API_URL)
  }
}
