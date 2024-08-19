package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.db.gallery.KeywordDao
import nasa.gallery.model.Keyword
import javax.inject.Inject

@Entity(tableName = "keyword")
data class RoomKeywordEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("keyword")
  val keyword: Keyword,
)

@Dao
interface RoomKeywordDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entity: RoomKeywordEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<RoomKeywordEntity>)
}

class RoomKeywordDaoWrapper @Inject constructor(db: RoomNasaDatabase) : KeywordDao {
  private val delegate = db.keywordDao()
  override suspend fun insert(keyword: Keyword) = delegate.insert(RoomKeywordEntity(keyword))
  override suspend fun insert(keywords: List<Keyword>) = delegate.insert(keywords.map(::RoomKeywordEntity))
}
