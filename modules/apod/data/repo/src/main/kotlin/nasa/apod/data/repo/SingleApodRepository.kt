package nasa.apod.data.repo

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import nasa.apod.data.api.ApodApi
import nasa.apod.data.api.ApodResponseModel
import nasa.apod.data.db.ApodDao
import nasa.apod.model.ApodItem
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

/**
 * Performs data layer access of [ApodItem]s from a local database primarily, or a remote API if not already cached.
 * Specifically, it handles requesting a single item from the API, which can be one of:
 * - today (date == null)
 * - one specific date
 * - one random date
 */
class SingleApodRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val api: ApodApi,
  private val dao: ApodDao,
  private val calendar: Calendar,
  private val sharedRepository: SharedRepository,
) {
  suspend fun loadToday(key: ApiKey): SingleLoadResult {
    return loadFromDatabaseOrApi(key, date = null, getResponse = { api.getToday(key) })
  }

  suspend fun loadSpecific(key: ApiKey, date: LocalDate): SingleLoadResult {
    return loadFromDatabaseOrApi(key, date, getResponse = { api.getByDate(key, date) })
  }

  suspend fun loadRandom(key: ApiKey): SingleLoadResult {
    // Can't query the DB if we don't know what date we want, so go straight to API
    val result = loadFromApi(
      key = key,
      date = null,
      getResponse = { api.getRandom(key) },
      parseBody = { sharedRepository.tidyItem(it.first().toItem()) },
    )
    saveToDatabaseIfSuccessful(result)
    return result
  }

  private suspend fun loadFromDatabaseOrApi(
    key: ApiKey,
    date: LocalDate?,
    getResponse: suspend () -> Response<ApodResponseModel>,
  ): SingleLoadResult {
    // First query the local database for info on this date's APOD entry
    val entity = withContext(io) {
      dao.get(date ?: calendar.today())
    }
    val itemFromDb = entity?.toItem()
    if (itemFromDb != null) {
      Timber.d("Fetched from DB: $itemFromDb")
      return SingleLoadResult.Success(itemFromDb)
    }

    // If it didn't exist locally, query the API to pull it down and store it in the DB
    val result = loadFromApi(
      key = key,
      date = date,
      getResponse = getResponse,
      parseBody = { sharedRepository.tidyItem(it.toItem()) },
    )
    saveToDatabaseIfSuccessful(result)
    return result
  }

  private suspend fun <ResponseType : Any> loadFromApi(
    key: ApiKey,
    date: LocalDate?,
    getResponse: suspend () -> Response<ResponseType>,
    parseBody: (ResponseType) -> ApodItem,
  ): SingleLoadResult {
    return try {
      val response = withContext(io) { getResponse() }
      if (response.isSuccessful) {
        // Received and parsed a-ok, so pass it down after removing pesky newlines and blank strings
        val body = response.body() ?: error("Null body")
        val item = parseBody(body)
        SingleLoadResult.Success(item)
      } else {
        // Attempt to decode the error response body as JSON to pull out the reason
        val errorBody = response.errorBody()?.string()
        return sharedRepository.parseHttpFailure(key, date, response.code(), errorBody)
      }
    } catch (e: Exception) {
      sharedRepository.parseFailure(e)
    }
  }

  private suspend fun saveToDatabaseIfSuccessful(result: SingleLoadResult) {
    if (result is SingleLoadResult.Success) {
      val item = result.item
      val entity = item.toEntity()
      withContext(io) { dao.insert(entity) }
      Timber.d("Stored $entity in database")
    }
  }
}
