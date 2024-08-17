package nasa.core.http

import alakazam.kotlin.core.StateHolder
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

class ApiUsageInterceptor @Inject constructor(
  private val stateHolder: ApiUsageStateHolder,
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())

    val remaining = response.intHeaderOrNull(key = "X-Ratelimit-Remaining")
    val upperLimit = response.intHeaderOrNull(key = "X-Ratelimit-Limit")
    if (remaining != null && upperLimit != null) {
      val usage = ApiUsage(remaining, upperLimit)
      stateHolder.set(usage)
    }

    return response
  }

  private fun Response.intHeaderOrNull(key: String): Int? = header(key, defaultValue = null)?.toIntOrNull()
}

data class ApiUsage(
  val remaining: Int,
  val upperLimit: Int,
)

@Singleton
class ApiUsageStateHolder @Inject constructor() : StateHolder<ApiUsage?>(initialState = null)
