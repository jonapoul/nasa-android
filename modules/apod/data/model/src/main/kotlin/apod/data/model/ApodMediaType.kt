package apod.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ApodMediaType {
  @SerialName("image")
  Image,

  @SerialName("video")
  Video,

  @SerialName("other")
  Other,
}
