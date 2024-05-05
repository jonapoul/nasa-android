package apod.android.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.model.ThemeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ApodActivityViewModel @Inject internal constructor(
  private val scope: CoroutineScope,
  themePreferences: ThemePreferences,
) : ViewModel() {
  val theme: StateFlow<ThemeType> = themePreferences
    .theme
    .asFlow()
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = themePreferences.theme.get())

  override fun onCleared() {
    Timber.v("onCleared")
    super.onCleared()
    scope.cancel()
  }
}
