package nasa.core.http.usage

import kotlinx.coroutines.flow.update
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiUsageInterceptor @Inject constructor(
  private val stateHolder: ApiUsageStateHolder,
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())

    val remaining = response.intHeaderOrNull(key = "X-Ratelimit-Remaining")
    val upperLimit = response.intHeaderOrNull(key = "X-Ratelimit-Limit")
    if (remaining != null && upperLimit != null) {
      val usage = ApiUsage(remaining, upperLimit)
      stateHolder.update { usage }
    }

    return response
  }

  private fun Response.intHeaderOrNull(key: String): Int? = header(key, defaultValue = null)?.toIntOrNull()
}
