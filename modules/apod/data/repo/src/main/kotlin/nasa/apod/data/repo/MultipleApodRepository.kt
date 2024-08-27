package nasa.apod.data.repo

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import nasa.apod.data.api.ApodApi
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import nasa.db.apod.ApodDao
import timber.log.Timber
import java.time.YearMonth
import javax.inject.Inject
import kotlin.random.Random

class MultipleApodRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val api: ApodApi,
  private val dao: ApodDao,
  private val calendar: Calendar,
  private val sharedRepository: SharedRepository,
) {
  suspend fun loadThisMonth(key: ApiKey): MultipleLoadResult {
    val today = calendar.today()
    val firstDate = LocalDate(today.year, today.month, dayOfMonth = 1).ensureWithinRange(today)
    val lastDate = findLastDate(firstDate).ensureWithinRange(today)
    return loadFromApiAndCache(key, firstDate, lastDate)
  }

  suspend fun loadSpecificMonth(key: ApiKey, date: LocalDate): MultipleLoadResult {
    val today = calendar.today()
    val firstDate = LocalDate(date.year, date.month, dayOfMonth = 1).ensureWithinRange(today)
    val lastDate = findLastDate(firstDate).ensureWithinRange(today)
    return loadFromApiAndCache(key, firstDate, lastDate)
  }

  suspend fun loadRandomMonth(key: ApiKey): MultipleLoadResult {
    val randomDate = LocalDate.fromEpochDays(
      epochDays = Random.nextInt(
        from = EARLIEST_APOD_DATE.toEpochDays(),
        until = calendar.today().toEpochDays(),
      ),
    )
    val today = calendar.today()
    val firstDate = LocalDate(randomDate.year, randomDate.month, dayOfMonth = 1).ensureWithinRange(today)
    val lastDate = findLastDate(firstDate).ensureWithinRange(today)
    return loadFromApiAndCache(key, firstDate, lastDate)
  }

  private suspend fun loadFromApiAndCache(key: ApiKey, start: LocalDate, end: LocalDate): MultipleLoadResult {
    val result = loadFromApi(key, start, end)
    if (result is MultipleLoadResult.Success) {
      val entities = result.items.map { it.toEntity() }
      withContext(io) { dao.insert(entities) }
      Timber.d("Stored ${entities.size} items in database")
    }
    return result
  }

  private suspend fun loadFromApi(key: ApiKey, start: LocalDate, end: LocalDate): MultipleLoadResult {
    return try {
      val response = withContext(io) { api.getRange(key, start, end) }
      if (response.isSuccessful) {
        // Received and parsed a-ok, so pass it down after removing pesky newlines and blank strings
        val body = response.body() ?: error("Null body")
        val item = body.map { sharedRepository.tidyItem(it.toItem()) }
        MultipleLoadResult.Success(item)
      } else {
        // Attempt to decode the error response body as JSON to pull out the reason
        val errorBody = response.errorBody()?.string()
        return sharedRepository.parseHttpFailure(key, start, response.code(), errorBody)
      }
    } catch (e: Exception) {
      sharedRepository.parseFailure(e)
    }
  }

  private fun findLastDate(firstDate: LocalDate): LocalDate {
    val finalDay = YearMonth.of(firstDate.year, firstDate.monthNumber).lengthOfMonth()
    return LocalDate(firstDate.year, firstDate.month, finalDay)
  }

  private fun LocalDate.ensureWithinRange(
    latest: LocalDate,
    earliest: LocalDate = EARLIEST_APOD_DATE,
  ): LocalDate = when {
    this < earliest -> earliest
    this > latest -> latest
    else -> this
  }
}
