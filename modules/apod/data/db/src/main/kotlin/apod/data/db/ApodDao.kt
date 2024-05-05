package apod.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface ApodDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: ApodEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(entities: List<ApodEntity>)

  @Query("SELECT * FROM apod WHERE date = :date LIMIT 1")
  suspend fun get(date: LocalDate): ApodEntity?

  @Query("SELECT date FROM apod")
  fun observeDates(): Flow<List<LocalDate>>

  @Query("SELECT COUNT(*) FROM apod")
  fun itemCount(): Flow<Int>

  @Query("DELETE FROM apod")
  suspend fun clear()
}
