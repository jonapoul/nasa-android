@file:UseSerializers(
  KeywordsSerializer::class,
  NasaIdSerializer::class,
  MediaTypeSerializer::class,
)

package nasa.gallery.data.api

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId

@Serializable(SearchResponseSerializer::class)
sealed interface SearchResponse {
  @Serializable
  data class Success(
    @SerialName("collection") val collection: SearchCollection,
  ) : SearchResponse

  @Serializable
  data class Failure(
    @SerialName("reason") val reason: String,
  ) : SearchResponse
}

@Serializable
data class SearchCollection(
  @SerialName("version") val version: String,
  @SerialName("href") val url: String,
  @SerialName("items") val items: List<SearchItem>,
  @SerialName("metadata") val metadata: SearchMetadata,
  @SerialName("links") val links: List<SearchLink>,
)

@Serializable
data class SearchItem(
  @SerialName("href") val collectionUrl: String,
  @SerialName("data") val data: List<SearchItemData>,
  @SerialName("links") val links: List<SearchItemLink>,
)

@Serializable
data class SearchItemData(
  @SerialName("center") val center: String,
  @SerialName("title") val title: String,
  @SerialName("keywords") val keywords: Keywords,
  @SerialName("nasa_id") val nasaId: NasaId,
  @SerialName("date_created") val dateCreated: Instant,
  @SerialName("media_type") val mediaType: MediaType,
  @SerialName("description") val description: String,
  @SerialName("description_508") val description508: String,
)

@Serializable
data class SearchItemLink(
  @SerialName("href") val url: String,
  @SerialName("rel") val rel: String,
  @SerialName("render") val render: String? = null,
)

@Serializable
data class SearchMetadata(
  @SerialName("total_hits") val totalHits: Int,
)

@Serializable
data class SearchLink(
  @SerialName("rel") val rel: String,
  @SerialName("prompt") val prompt: String,
  @SerialName("href") val url: String,
)
