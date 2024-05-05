package apod.core.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun buildOkHttp(interceptor: Interceptor? = null, log: ((String) -> Unit)?): OkHttpClient {
  val builder = OkHttpClient.Builder()

  if (log != null) {
    val logger = HttpLoggingInterceptor { log(it) }
    logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    builder.addInterceptor(logger)
  }

  if (interceptor != null) {
    builder.addInterceptor(interceptor)
  }

  return builder.build()
}
