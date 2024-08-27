package nasa.db.gallery

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.gallery.model.Photographer

@Entity(tableName = "photographer")
data class PhotographerEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("photographer")
  val photographer: Photographer,
)

@Dao
interface PhotographerDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entity: PhotographerEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<PhotographerEntity>)
}
