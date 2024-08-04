@file:UseSerializers(KeywordsSerializer::class)

package nasa.gallery.data.api

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import nasa.gallery.model.Center
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.gallery.model.Photographer

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
  @SerialName("links") val links: List<SearchLink>?,
)

@Serializable
data class SearchItem(
  @SerialName("href") val collectionUrl: JsonUrl,
  @SerialName("data") val data: List<SearchItemData>,
  @SerialName("links") val links: List<SearchItemLink>,
)

@Serializable
data class SearchItemData(
  @SerialName("center") val center: Center,
  @SerialName("title") val title: String,
  @SerialName("keywords") val keywords: Keywords?,
  @SerialName("location") val location: String?,
  @SerialName("photographer") val photographer: Photographer?,
  @SerialName("nasa_id") val nasaId: NasaId,
  @SerialName("date_created") val dateCreated: Instant,
  @SerialName("media_type") val mediaType: MediaType,
  @SerialName("description") val description: String,
  @SerialName("description_508") val description508: String?,
)

@Serializable
data class SearchItemLink(
  @SerialName("href") val url: ImageUrl,
  @SerialName("rel") val rel: Relation,
  @SerialName("render") val render: String? = null,
) {
  @Serializable
  enum class Relation {
    @SerialName("preview") Preview,
    @SerialName("captions") Captions,
  }
}

@Serializable
data class SearchMetadata(
  @SerialName("total_hits") val totalHits: Int,
)

@Serializable
data class SearchLink(
  @SerialName("rel") val rel: Relation,
  @SerialName("prompt") val prompt: String,
  @SerialName("href") val url: String,
) {
  @Serializable
  enum class Relation {
    @SerialName("next") Next,
    @SerialName("prev") Previous,
  }
}
