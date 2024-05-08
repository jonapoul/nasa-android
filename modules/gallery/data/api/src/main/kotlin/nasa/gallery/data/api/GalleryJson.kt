package nasa.gallery.data.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val GalleryJson = Json {
  explicitNulls = false
  ignoreUnknownKeys = true
}
