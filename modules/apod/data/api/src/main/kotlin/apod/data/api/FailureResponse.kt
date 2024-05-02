package apod.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FailureResponse(
  @SerialName("code") val code: Int,
  @SerialName("msg") val message: String,
)
