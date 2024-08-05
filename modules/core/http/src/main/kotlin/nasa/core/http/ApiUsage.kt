package nasa.core.http

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiUsageInterceptor @Inject constructor(
  private val dataStore: ApiUsageDataStore,
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())

    val remaining = response.intHeaderOrNull(key = "X-Ratelimit-Remaining")
    val upperLimit = response.intHeaderOrNull(key = "X-Ratelimit-Limit")
    if (remaining != null && upperLimit != null) {
      val usage = ApiUsage(remaining, upperLimit)
      dataStore.store(usage)
    }

    return response
  }

  private fun Response.intHeaderOrNull(key: String): Int? = header(key, defaultValue = null)?.toIntOrNull()
}

fun interface ApiUsageDataStore {
  fun store(usage: ApiUsage)
}

fun interface ApiUsageDataSource {
  fun observe(): Flow<ApiUsageState>
}

data class ApiUsage(
  val remaining: Int,
  val upperLimit: Int,
)

@Immutable
sealed interface ApiUsageState {
  sealed interface HasDemoKey : ApiUsageState

  sealed interface NoUsage : ApiUsageState

  sealed interface HasUsage : ApiUsageState {
    val remaining: Int
    val upperLimit: Int
  }

  data object NoApiKey : ApiUsageState

  data object DemoKeyNoUsage : HasDemoKey, NoUsage

  data object RealKeyNoUsage : NoUsage

  data class DemoKeyHasUsage(
    override val remaining: Int,
    override val upperLimit: Int,
  ) : HasUsage, HasDemoKey

  data class RealKeyHasUsage(
    override val remaining: Int,
    override val upperLimit: Int,
  ) : HasUsage
}
