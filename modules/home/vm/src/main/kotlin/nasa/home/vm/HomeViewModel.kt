package nasa.home.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import nasa.core.http.ApiUsage
import nasa.core.http.ApiUsageStateHolder
import nasa.core.model.ApiKey
import nasa.core.model.NASA_API_URL
import nasa.core.url.UrlOpener
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
  private val stateHolder: ApiUsageStateHolder,
  private val urlOpener: UrlOpener,
  private val provider: ApiKey.Provider,
) : ViewModel() {
  fun apiUsage(): Flow<ApiUsageState> = combine(stateHolder.state, provider.observe(), ::toState)

  fun registerForApiKey() = urlOpener.openUrl(NASA_API_URL)

  private fun toState(usage: ApiUsage?, key: ApiKey?): ApiUsageState = when {
    key == null -> ApiUsageState.NoApiKey

    key == ApiKey.DEMO -> if (usage == null) {
      ApiUsageState.DemoKeyNoUsage
    } else {
      ApiUsageState.DemoKeyHasUsage(usage.remaining, usage.upperLimit)
    }

    usage == null -> ApiUsageState.RealKeyNoUsage

    else -> ApiUsageState.RealKeyHasUsage(usage.remaining, usage.upperLimit)
  }
}
