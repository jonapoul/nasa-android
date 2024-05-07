package nasa.apod.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ApodMediaType {
  @SerialName("image")
  Image,

  @SerialName("video")
  Video,

  // Only ever seen this in 2009-04-05: https://apod.nasa.gov/apod/ap090405.html. No image, no video?
  @SerialName("other")
  Other,
}
