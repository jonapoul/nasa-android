package nasa.db.apod

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ApodDao {
  suspend fun insert(entity: ApodEntity)
  suspend fun insertAll(entities: List<ApodEntity>)
  suspend fun get(date: LocalDate): ApodEntity?
  fun observeDates(): Flow<List<LocalDate>>
}
