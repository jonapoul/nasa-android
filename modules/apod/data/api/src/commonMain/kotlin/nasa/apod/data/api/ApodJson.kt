package nasa.apod.data.api

import kotlinx.serialization.json.Json

val ApodJson = Json {
  explicitNulls = false
  ignoreUnknownKeys = true
}
