package nasa.android.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import nasa.core.model.ThemeType
import javax.inject.Inject

@HiltViewModel
class NasaActivityViewModel @Inject internal constructor(
  private val themePreferences: ThemePreferences,
) : ViewModel() {
  val theme: StateFlow<ThemeType> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val theme by themePreferences
      .theme
      .asFlow()
      .collectAsState(initial = themePreferences.theme.get())
    theme
  }
}
