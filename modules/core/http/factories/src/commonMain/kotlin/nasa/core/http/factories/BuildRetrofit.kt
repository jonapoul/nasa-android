package nasa.core.http.factories

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

fun buildRetrofit(
  client: OkHttpClient,
  url: String,
  json: Json,
): Retrofit {
  val contentType = "application/json".toMediaType()
  return buildRetrofit(client, url, json.asConverterFactory(contentType))
}

fun buildRetrofit(
  client: OkHttpClient,
  url: String,
  vararg factories: Converter.Factory,
): Retrofit = Retrofit
  .Builder()
  .apply { factories.forEach { addConverterFactory(it) } }
  .client(client)
  .baseUrl(url)
  .build()
