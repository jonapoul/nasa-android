package nasa.gallery.data.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryResponseModel(
  @SerialName("date")
  val date: LocalDate,
)
