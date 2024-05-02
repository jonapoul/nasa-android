package apod.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.requireMessage
import apod.core.model.ApiKeyProvider
import apod.data.api.ApodApi
import apod.data.api.SingleApodResponse
import apod.data.db.ApodDao
import apod.data.model.ApodItem
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Performs data layer access of [ApodItem]s from a local database primarily, or a remote API if not already cached.
 */
class ApodRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val api: ApodApi,
  private val dao: ApodDao,
  apiKeyProvider: ApiKeyProvider,
) {
  private val apiKey = apiKeyProvider.get()

  suspend fun loadApodItem(requested: LocalDate?, today: LocalDate): LoadResult {
    // First query the local database for info on this date's APOD entry
    val itemFromDb = dao.get(requested ?: today)?.toItem()
    if (itemFromDb != null) {
      Timber.d("Fetched from DB: $itemFromDb")
      return LoadResult.Success(itemFromDb)
    }

    // If it didn't exist locally, query the API to pull it down and store it in the DB
    when (val response = fetchFromApi(requested)) {
      is SingleApodResponse.Success -> {
        val itemFromApi = response.item.toItem()
        saveToDb(itemFromApi)
        Timber.d("Fetched from API: $itemFromApi")
        return LoadResult.Success(itemFromApi)
      }

      is SingleApodResponse.Failure -> {
        Timber.w("Failed fetching from API: $response")
        return LoadResult.Failure(reason = response.fullMessage())
      }
    }
  }

  private suspend fun fetchFromApi(date: LocalDate?): SingleApodResponse {
    return try {
      if (date == null) {
        withContext(io) { api.getToday(apiKey) }
      } else {
        withContext(io) { api.getByDate(apiKey, date) }
      }
    } catch (e: Exception) {
      // TODO: more specific error handling
      Timber.e(e, "Failed fetching $date from API with key $apiKey")
      SingleApodResponse.Failure(
        code = "Unknown",
        message = e.requireMessage(),
      )
    }
  }

  private suspend fun saveToDb(item: ApodItem) {
    val entity = item.toEntity()
    withContext(io) { dao.insert(entity) }
  }
}
