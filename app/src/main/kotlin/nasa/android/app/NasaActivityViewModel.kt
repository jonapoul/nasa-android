package nasa.android.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import nasa.core.model.ThemeType
import javax.inject.Inject

@HiltViewModel
internal class NasaActivityViewModel @Inject constructor(
  themePreferences: ThemePreferences,
) : ViewModel() {
  val theme: StateFlow<ThemeType> = themePreferences
    .theme
    .asFlow()
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = themePreferences.theme.get())
}
