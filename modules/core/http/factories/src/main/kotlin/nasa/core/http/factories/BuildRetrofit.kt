package nasa.core.http.factories

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

fun buildRetrofit(client: OkHttpClient, url: String, json: Json = Json): Retrofit {
  val contentType = "application/json".toMediaType()
  return Retrofit
    .Builder()
    .addConverterFactory(json.asConverterFactory(contentType))
    .client(client)
    .baseUrl(url)
    .build()
}
