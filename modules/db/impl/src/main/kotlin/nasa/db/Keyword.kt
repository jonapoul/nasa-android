package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import nasa.db.gallery.KeywordDao
import nasa.gallery.model.Keyword

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
  suspend fun insertAll(entities: List<RoomKeywordEntity>)

  @Query("SELECT * FROM keyword ORDER BY keyword DESC")
  suspend fun getAll(): List<RoomKeywordEntity>

  @Query("DELETE FROM keyword")
  suspend fun clear()
}

class RoomKeywordDaoWrapper(private val delegate: RoomKeywordDao) : KeywordDao {
  override suspend fun insert(keyword: Keyword) = delegate.insert(RoomKeywordEntity(keyword))

  override suspend fun insertAll(keywords: List<Keyword>) = delegate.insertAll(keywords.map(::RoomKeywordEntity))

  override suspend fun getAll() = delegate.getAll().map { it.keyword }

  override suspend fun clear() = delegate.clear()
}
