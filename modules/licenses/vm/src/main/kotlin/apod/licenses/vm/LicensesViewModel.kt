package apod.licenses.vm

import alakazam.kotlin.core.exhaustive
import alakazam.kotlin.core.requireMessage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.core.url.UrlOpener
import apod.licenses.data.LicensesLoadState
import apod.licenses.data.LicensesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject constructor(
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
        is LicensesLoadState.Failure -> LicensesState.Error(loadState.cause.requireMessage())
        is LicensesLoadState.Success -> loadState.toLicensesState()
      }.exhaustive

      mutableState.update { licensesState }
    }
  }

  private fun LicensesLoadState.Success.toLicensesState(): LicensesState {
    return if (libraries.isEmpty()) {
      LicensesState.NoneFound
    } else {
      LicensesState.Loaded(libraries.toImmutableList())
    }
  }

  fun openUrl(url: String) {
    Timber.v("openUrl $url")
    urlOpener.openUrl(url)
  }
}
