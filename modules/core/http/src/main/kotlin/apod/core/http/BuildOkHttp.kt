package apod.core.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun buildOkHttp(
  debug: Boolean,
  vararg interceptors: Interceptor,
  log: ((String) -> Unit)?,
): OkHttpClient {
  val builder = OkHttpClient.Builder()

  if (debug && log != null) {
    val logger = HttpLoggingInterceptor { log(it) }
    logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    builder.addInterceptor(logger)
  }

  interceptors.forEach {
    builder.addInterceptor(it)
  }

  return builder.build()
}
