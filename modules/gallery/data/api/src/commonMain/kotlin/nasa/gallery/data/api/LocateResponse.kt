package nasa.gallery.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(LocateResponseSerializer::class)
sealed interface LocateResponse {
  @Serializable
  data class Success(
    @SerialName("location") val location: String,
  ) : LocateResponse

  @Serializable
  data class Failure(
    @SerialName("reason") val reason: String,
  ) : LocateResponse
}
