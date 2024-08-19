package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.db.gallery.CenterDao
import nasa.gallery.model.Center
import javax.inject.Inject

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
  suspend fun insert(entities: List<RoomCenterEntity>)
}

class RoomCenterDaoWrapper @Inject constructor(db: RoomNasaDatabase) : CenterDao {
  private val delegate = db.centreDao()
  override suspend fun insert(center: Center) = delegate.insert(RoomCenterEntity(center))
  override suspend fun insert(centers: List<Center>) = delegate.insert(centers.map(::RoomCenterEntity))
}
