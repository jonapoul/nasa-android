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
    @SerialName("code") override val code: String,
    @SerialName("message") override val message: String,
  ) : SingleApodResponse, FailureResponse
}

@Serializable(MultipleResponseSerializer::class)
sealed interface MultipleApodResponse {
  @Serializable
  data class Success(
    val items: List<ApodResponseItem>,
  ) : MultipleApodResponse

  @Serializable
  data class Failure(
    @SerialName("code") override val code: String,
    @SerialName("message") override val message: String,
  ) : MultipleApodResponse, FailureResponse
}

interface FailureResponse {
  val code: String
  val message: String

  fun fullMessage(): String = "$code - $message"
}
