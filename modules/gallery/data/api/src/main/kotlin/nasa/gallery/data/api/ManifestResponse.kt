package nasa.gallery.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(ManifestResponseSerializer::class)
sealed interface ManifestResponse {
  @Serializable
  data class Success(
    @SerialName("collection") val collection: ManifestCollection,
  ) : ManifestResponse

  @Serializable
  data class Failure(
    @SerialName("reason") val reason: String,
  ) : ManifestResponse
}

@Serializable
data class ManifestCollection(
  @SerialName("version") val version: String,
  @SerialName("href") val url: String,
  @SerialName("items") val items: List<ManifestItem>,
)

@Serializable
data class ManifestItem(
  @SerialName("href") val url: String,
)
