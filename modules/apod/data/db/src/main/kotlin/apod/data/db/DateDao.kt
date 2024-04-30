package apod.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.datetime.LocalDate

@Dao
interface DateDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: DateEntity)

  @Query("SELECT * FROM date WHERE date = :date LIMIT 1")
  suspend fun get(date: LocalDate): DateEntity?
}
