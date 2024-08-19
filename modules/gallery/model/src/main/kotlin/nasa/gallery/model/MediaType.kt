package nasa.gallery.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MediaType(private val string: String) {
  @SerialName("audio") Audio(string = "audio"),
  @SerialName("image") Image(string = "image"),
  @SerialName("video") Video(string = "video"),
  ;

  override fun toString() = string
}
