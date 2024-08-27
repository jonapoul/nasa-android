package nasa.db.gallery

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.gallery.model.Center

@Entity(tableName = "center")
data class CenterEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("center")
  val center: Center,
)

@Dao
interface CenterDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entity: CenterEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<CenterEntity>)
}
