package nasa.db.gallery

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.gallery.model.Keyword

@Entity(tableName = "keyword")
data class KeywordEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("keyword")
  val keyword: Keyword,
)

@Dao
interface KeywordDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entity: KeywordEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<KeywordEntity>)
}
