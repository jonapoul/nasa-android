package nasa.apod.data.repo

import alakazam.kotlin.core.requireMessage
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerializationException
import nasa.apod.data.api.ApodJson
import nasa.apod.data.api.FailureResponse
import nasa.apod.model.ApodItem
import nasa.core.model.ApiKey
import java.io.IOException
import javax.inject.Inject

internal class SharedRepository @Inject constructor() {
  fun parseFailure(e: Exception): FailureResult = when (e) {
    is SerializationException -> FailureResult.Json(e.requireMessage())
    is IOException -> FailureResult.Network
    else -> FailureResult.Other(e.requireMessage())
  }

  fun parseHttpFailure(key: ApiKey, date: LocalDate?, code: Int, body: String?): FailureResult {
    if (body.isNullOrBlank()) {
      return FailureResult.OtherHttp(code, message = "Empty error body")
    }

    if (code == CODE_FORBIDDEN) {
      // Unauthorised, so an invalid API key is loaded into the app
      return FailureResult.InvalidAuth(key)
    } else if (code == CODE_TOO_MANY_REQUESTS) {
      return FailureResult.RateLimitExceeded(key)
    }

    // Expect response in the format: { "code": 400, "msg": "Some message", "service_version": "v1" }
    val message = ApodJson.decodeFromString(FailureResponse.serializer(), body).message

    return when {
      // In this case, the returned message contains the full allowed range. We can just show it to the user directly
      code == CODE_BAD_REQUEST ->
        FailureResult.OutOfRange(message)

      code == CODE_NOT_FOUND && message.contains(NO_APOD_MESSAGE) ->
        FailureResult.NoApod(date = date ?: error("Expected non-null date"))

      else ->
        FailureResult.OtherHttp(code, message)
    }
  }

  // Values received from the API sometimes have newlines at the start or end of the string. That makes it awkward to
  // display in UI, so clip them off here before saving them to the database
  fun tidyItem(item: ApodItem) = item.copy(
    title = item.title.trimUnwantedChars(),
    explanation = item.explanation.trimUnwantedChars(),

    // Copyright sometimes has newlines scattered in the middle of the string, so just remove them all for sanity
    copyright = item.copyright
      ?.replace(oldValue = "\n", newValue = " ")
      ?.trim()
      ?.takeIf { it.isNotBlank() },
  )

  private fun String.trimUnwantedChars(): String = trim('\n', ' ')

  private companion object {
    // Response message given if a date is selected with no associated APOD
    const val NO_APOD_MESSAGE = "No data available for date"

    const val CODE_BAD_REQUEST = 400
    const val CODE_FORBIDDEN = 403
    const val CODE_NOT_FOUND = 404
    const val CODE_TOO_MANY_REQUESTS = 429
  }
}
