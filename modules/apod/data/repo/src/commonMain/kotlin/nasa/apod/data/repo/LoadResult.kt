package nasa.apod.data.repo

import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.core.model.ApiKey

sealed interface SingleLoadResult {
  data class Success(val item: ApodItem) : SingleLoadResult
}

sealed interface MultipleLoadResult {
  data class Success(val items: List<ApodItem>) : MultipleLoadResult
}

sealed interface FailureResult : SingleLoadResult, MultipleLoadResult {
  // Date must be from 16/06/1995 to current date
  data class OutOfRange(val message: String) : FailureResult

  // No APOD was provided for the given date, even though it's within range. More common in early APOD days
  data class NoApod(val date: LocalDate) : FailureResult

  // Unexpected JSON format when parsing a response
  data class InvalidAuth(val key: ApiKey) : FailureResult

  // The rate limit associated with your API key was exceeded
  data class RateLimitExceeded(val key: ApiKey) : FailureResult

  // HTTP failure beyond those above
  data class OtherHttp(val code: Int, val message: String?) : FailureResult

  // Unexpected JSON format when parsing a response
  data class Json(val message: String) : FailureResult

  // Network problem - probably don't have internet on the phone
  data object Network : FailureResult

  // Something else?
  data class Other(val message: String) : FailureResult
}

fun FailureResult.reason(): String = when (this) {
  is FailureResult.OutOfRange -> message
  is FailureResult.NoApod -> "No APOD exists for $date"
  is FailureResult.InvalidAuth -> "Invalid API key - $key"
  is FailureResult.RateLimitExceeded -> "Rate limit exceeded"
  is FailureResult.OtherHttp -> "HTTP code $code - $message"
  is FailureResult.Json -> "Failed parsing response from server"
  FailureResult.Network -> "Network problem: does your phone have an internet connection?"
  is FailureResult.Other -> "Unexpected problem: $message"
}
