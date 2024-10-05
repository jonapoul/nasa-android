package nasa.apod.data.repo

import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.db.ApodDao
import javax.inject.Inject

class LocalDatabaseRepository @Inject internal constructor(
  private val apodDao: ApodDao,
) {
  suspend fun get(date: LocalDate): ApodItem {
    val entity = apodDao.get(date) ?: error("No item found with date $date")
    return entity.toItem()
  }
}
