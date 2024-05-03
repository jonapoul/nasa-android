package apod.data.repo

import apod.core.model.ApiKey
import apod.core.model.ApodItem
import kotlinx.datetime.LocalDate

sealed interface LoadResult {
  data class Success(val item: ApodItem) : LoadResult

  sealed interface Failure : LoadResult {
    // Date must be from 16/06/1995 to current date
    data class OutOfRange(val message: String) : Failure

    // No APOD was provided for the given date, even though it's within range. More common in early APOD days
    data class NoApod(val date: LocalDate) : Failure

    // Unexpected JSON format when parsing a response
    data class InvalidAuth(val key: ApiKey) : Failure

    // HTTP failure beyond those above
    data class OtherHttp(val code: Int, val message: String?) : Failure

    // Unexpected JSON format when parsing a response
    data class Json(val message: String) : Failure

    // Network problem - probably don't have internet on the phone
    data object Network : Failure

    // Something else?
    data class Other(val message: String) : Failure
  }
}
