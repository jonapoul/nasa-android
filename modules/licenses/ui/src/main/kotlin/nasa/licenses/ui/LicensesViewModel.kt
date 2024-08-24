package nasa.licenses.ui

import alakazam.kotlin.core.exhaustive
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nasa.core.url.UrlOpener
import nasa.licenses.data.LicensesLoadState
import nasa.licenses.data.LicensesRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class LicensesViewModel @Inject constructor(
  private val licensesRepository: LicensesRepository,
  private val urlOpener: UrlOpener,
) : ViewModel() {
  private val mutableState = MutableStateFlow<LicensesState>(LicensesState.Loading)
  val state: StateFlow<LicensesState> = mutableState.asStateFlow()

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
      }.exhaustive

      mutableState.update { licensesState }
    }
  }

  private fun LicensesLoadState.Success.toLicensesState(): LicensesState = if (libraries.isEmpty()) {
    LicensesState.NoneFound
  } else {
    LicensesState.Loaded(libraries.toImmutableList())
  }

  fun openUrl(url: String) {
    Timber.v("openUrl $url")
    urlOpener.openUrl(url)
  }
}
