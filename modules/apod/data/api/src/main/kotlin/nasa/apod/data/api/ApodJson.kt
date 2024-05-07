package nasa.apod.data.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val ApodJson = Json {
  explicitNulls = false
  ignoreUnknownKeys = true
}
