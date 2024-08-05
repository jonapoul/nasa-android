package nasa.home.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import nasa.core.http.ApiUsageDataSource
import nasa.core.http.ApiUsageState
import nasa.core.model.NASA_API_URL
import nasa.core.url.UrlOpener
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
  private val apiUsageSource: ApiUsageDataSource,
  private val urlOpener: UrlOpener,
) : ViewModel() {
  fun apiUsage(): Flow<ApiUsageState> = apiUsageSource.observe()

  fun registerForApiKey() = urlOpener.openUrl(NASA_API_URL)
}
