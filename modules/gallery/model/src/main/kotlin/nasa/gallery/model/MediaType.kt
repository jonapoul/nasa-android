package nasa.gallery.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MediaType {
  @SerialName("audio") Audio,
  @SerialName("image") Image,
  @SerialName("video") Video,
}
