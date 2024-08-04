package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import nasa.db.gallery.CenterDao
import nasa.gallery.model.Center

@Entity(tableName = "center")
data class RoomCenterEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("center")
  val center: Center,
)

@Dao
interface RoomCenterDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entity: RoomCenterEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAll(entities: List<RoomCenterEntity>)

  @Query("SELECT * FROM center ORDER BY center DESC")
  suspend fun getAll(): List<RoomCenterEntity>

  @Query("DELETE FROM center")
  suspend fun clear()
}

class RoomCenterDaoWrapper(private val delegate: RoomCenterDao) : CenterDao {
  override suspend fun insert(center: Center) =
    delegate.insert(RoomCenterEntity(center))

  override suspend fun insertAll(centers: List<Center>) =
    delegate.insertAll(centers.map(::RoomCenterEntity))

  override suspend fun getAll() = delegate.getAll().map { it.center }

  override suspend fun clear() = delegate.clear()
}
