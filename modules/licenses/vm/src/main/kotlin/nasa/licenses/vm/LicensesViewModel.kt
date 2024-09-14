package nasa.licenses.vm

import alakazam.android.core.UrlOpener
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nasa.licenses.data.LicensesLoadState
import nasa.licenses.data.LicensesRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject internal constructor(
  private val licensesRepository: LicensesRepository,
  private val urlOpener: UrlOpener,
) : ViewModel() {
  private val mutableState = MutableStateFlow<LicensesState>(LicensesState.Loading)

  val state: StateFlow<LicensesState> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val mutableState by mutableState.collectAsState()
    mutableState
  }

  init {
    load()
  }

  fun load() {
    Timber.v("load")
    mutableState.update { LicensesState.Loading }
    viewModelScope.launch {
      val licensesState = when (val loadState = licensesRepository.loadLicenses()) {
        is LicensesLoadState.Failure -> LicensesState.Error(loadState.cause)
        is LicensesLoadState.Success -> loadState.toLicensesState()
      }

      mutableState.update { licensesState }
    }
  }

  private fun LicensesLoadState.Success.toLicensesState() = if (libraries.isEmpty()) {
    LicensesState.NoneFound
  } else {
    LicensesState.Loaded(libraries.toImmutableList())
  }

  fun openUrl(url: String) {
    Timber.v("openUrl $url")
    urlOpener.openUrl(url)
  }
}
