package nasa.core.http.progress

import okhttp3.Interceptor
import okhttp3.Response

class DownloadProgressInterceptor(private val stateHolder: DownloadProgressStateHolder) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalResponse = chain.proceed(chain.request())
    val responseBuilder = originalResponse.newBuilder()
    val originalBody = originalResponse.body

    val identifier = originalResponse.request.header(HEADER)
    if (!identifier.isNullOrEmpty() && originalBody != null) {
      val body = DownloadProgressResponseBody(
        url = originalResponse.request.url.toString(),
        responseBody = originalBody,
        stateHolder = stateHolder,
      )
      responseBuilder.body(body)
    }

    return responseBuilder.build()
  }

  companion object {
    const val HEADER = "download-identifier"
  }
}
