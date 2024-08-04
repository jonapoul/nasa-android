package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import nasa.db.gallery.PhotographerDao
import nasa.gallery.model.Photographer

@Entity(tableName = "photographer")
data class RoomPhotographerEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("photographer")
  val photographer: Photographer,
)

@Dao
interface RoomPhotographerDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entity: RoomPhotographerEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAll(entities: List<RoomPhotographerEntity>)

  @Query("SELECT * FROM photographer ORDER BY photographer DESC")
  suspend fun getAll(): List<RoomPhotographerEntity>

  @Query("DELETE FROM photographer")
  suspend fun clear()
}

class RoomPhotographerDaoWrapper(private val delegate: RoomPhotographerDao) : PhotographerDao {
  override suspend fun insert(photographer: Photographer) =
    delegate.insert(RoomPhotographerEntity(photographer))

  override suspend fun insertAll(photographers: List<Photographer>) =
    delegate.insertAll(photographers.map(::RoomPhotographerEntity))

  override suspend fun getAll() = delegate.getAll().map { it.photographer }

  override suspend fun clear() = delegate.clear()
}
