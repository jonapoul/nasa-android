package apod.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.requireMessage
import apod.core.model.ApiKeyProvider
import apod.core.model.ApodItem
import apod.core.model.Calendar
import apod.data.api.ApodApi
import apod.data.api.ApodJson
import apod.data.api.FailureResponse
import apod.data.db.ApodDao
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Performs data layer access of [ApodItem]s from a local database primarily, or a remote API if not already cached.
 */
class ApodRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val api: ApodApi,
  private val dao: ApodDao,
  private val calendar: Calendar,
  private val apiKeyProvider: ApiKeyProvider,
) {
  suspend fun loadApodItem(date: LocalDate?): LoadResult {
    // First query the local database for info on this date's APOD entry
    val today = calendar.today()
    val itemFromDb = withContext(io) { dao.get(date ?: today) }?.toItem()
    if (itemFromDb != null) {
      Timber.d("Fetched from DB: $itemFromDb")
      return LoadResult.Success(itemFromDb)
    }

    // If it didn't exist locally, query the API to pull it down and store it in the DB
    val result = loadFromApi(date)
    if (result is LoadResult.Success) {
      val item = result.item
      val entity = item.toEntity()
      withContext(io) { dao.insert(entity) }
      Timber.d("Fetched from API: $item")
    }
    return result
  }

  private suspend fun loadFromApi(date: LocalDate?): LoadResult {
    return try {
      val apiKey = apiKeyProvider.get()
      val response = if (date == null) {
        withContext(io) { api.getToday(apiKey) }
      } else {
        withContext(io) { api.getByDate(apiKey, date) }
      }

      if (response.isSuccessful) {
        // Received and parsed a-ok, so pass it down after removing pesky newlines and blank strings
        val body = response.body() ?: error("Null body")
        val item = body.toItem().trimmed()
        LoadResult.Success(item)
      } else {
        // Attempt to decode the error response body as JSON to pull out the reason
        val errorBody = response.errorBody()?.string()
        return parseHttpFailure(date, response.code(), errorBody)
      }
    } catch (e: SerializationException) {
      Timber.e(e, "Failed deserializing response")
      LoadResult.Failure.Json(e.requireMessage())
    } catch (e: IOException) {
      Timber.w(e, "Probably don't have internet on the phone")
      LoadResult.Failure.Network
    } catch (e: Exception) {
      Timber.w(e, "Failed fetching $date from API")
      LoadResult.Failure.Other(message = e.requireMessage())
    }
  }

  private fun parseHttpFailure(date: LocalDate?, code: Int, body: String?): LoadResult.Failure {
    if (body.isNullOrBlank()) {
      return LoadResult.Failure.OtherHttp(code, message = "Empty error body")
    }

    if (code == CODE_FORBIDDEN) {
      // Unauthorised, so an invalid API key is loaded into the app
      return LoadResult.Failure.InvalidAuth(key = apiKeyProvider.get())
    }

    // Expect response in the format: { "code": 400, "msg": "Some message", "service_version": "v1" }
    val message = ApodJson.decodeFromString(FailureResponse.serializer(), body).message

    date ?: error("Expected non-null date")
    return when {
      // In this case, the returned message contains the full allowed range. We can just show it to the user directly
      code == CODE_BAD_REQUEST ->
        LoadResult.Failure.OutOfRange(message)

      code == CODE_NOT_FOUND && message.contains(NO_APOD_MESSAGE) ->
        LoadResult.Failure.NoApod(date)

      else ->
        LoadResult.Failure.OtherHttp(code, message)
    }
  }

  // Values received from the API sometimes have newlines at the start or end of the string. That makes it awkward to
  // display in UI, so clip them off here before saving them to the database
  private fun ApodItem.trimmed() = copy(
    title = title.trimUnwantedChars(),
    explanation = explanation.trimUnwantedChars(),

    // Copyright sometimes has newlines scattered in the middle of the string, so just remove them all for sanity
    copyright = copyright
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
  }
}
