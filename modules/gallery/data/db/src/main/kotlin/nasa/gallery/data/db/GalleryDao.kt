package nasa.gallery.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface GalleryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: GalleryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(entities: List<GalleryEntity>)

  @Query("SELECT * FROM gallery WHERE date = :date LIMIT 1")
  suspend fun get(date: LocalDate): GalleryEntity?

  @Query("SELECT date FROM gallery")
  fun observeDates(): Flow<List<LocalDate>>

  @Query("SELECT COUNT(*) FROM gallery")
  fun itemCount(): Flow<Int>

  @Query("DELETE FROM gallery")
  suspend fun clear()
}
