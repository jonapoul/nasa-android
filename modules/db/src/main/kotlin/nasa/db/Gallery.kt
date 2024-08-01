package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.datetime.LocalDate
import nasa.gallery.data.db.GalleryDao
import nasa.gallery.data.db.GalleryEntity

@Entity(tableName = "gallery")
data class RoomGalleryEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("date")
  override val date: LocalDate,
) : GalleryEntity

@Dao
interface RoomGalleryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: RoomGalleryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(entities: List<RoomGalleryEntity>)

  @Query("SELECT * FROM gallery WHERE date = :date LIMIT 1")
  suspend fun get(date: LocalDate): RoomGalleryEntity?

  @Query("DELETE FROM gallery")
  suspend fun clear()
}

class RoomGalleryDaoWrapper(private val delegate: RoomGalleryDao) : GalleryDao {
  override suspend fun insert(entity: GalleryEntity) = delegate.insert(entity.toImpl())
  override suspend fun insertAll(entities: List<GalleryEntity>) = delegate.insertAll(entities.map { it.toImpl() })
  override suspend fun get(date: LocalDate): GalleryEntity? = delegate.get(date)
  override suspend fun clear() = delegate.clear()

  private fun GalleryEntity.toImpl(): RoomGalleryEntity = if (this is RoomGalleryEntity) {
    this
  } else {
    RoomGalleryEntity(date)
  }
}
