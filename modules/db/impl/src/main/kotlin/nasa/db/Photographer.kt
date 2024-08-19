package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.db.gallery.PhotographerDao
import nasa.gallery.model.Photographer
import javax.inject.Inject

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
}

class RoomPhotographerDaoWrapper @Inject constructor(db: RoomNasaDatabase) : PhotographerDao {
  private val delegate = db.photographerDao()

  override suspend fun insert(photographer: Photographer) =
    delegate.insert(RoomPhotographerEntity(photographer))

  override suspend fun insert(photographers: List<Photographer>) =
    delegate.insertAll(photographers.map(::RoomPhotographerEntity))
}
