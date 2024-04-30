package apod.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(SingleResponseSerializer::class)
sealed interface SingleApodResponse {
  @Serializable
  data class Success(
    val item: ApodResponseItem,
  ) : SingleApodResponse

  @Serializable
  data class Failure(
    @SerialName("code") val code: String,
    @SerialName("message") val message: String,
  ) : SingleApodResponse
}

@Serializable(MultipleResponseSerializer::class)
sealed interface MultipleApodResponse {
  @Serializable
  data class Success(
    val items: List<ApodResponseItem>,
  ) : MultipleApodResponse

  @Serializable
  data class Failure(
    @SerialName("code") val code: String,
    @SerialName("message") val message: String,
  ) : MultipleApodResponse
}
