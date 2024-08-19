package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import nasa.db.gallery.GalleryDao
import nasa.db.gallery.GalleryEntity
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import javax.inject.Inject

@Entity(tableName = "gallery")
data class RoomGalleryEntity(
  @PrimaryKey
  @ColumnInfo(name = "id")
  override val id: NasaId,

  @ColumnInfo(name = "collectionUrl", defaultValue = "")
  override val collectionUrl: JsonUrl,

  @ColumnInfo(name = "mediaType", defaultValue = "image")
  override val mediaType: MediaType,
) : GalleryEntity

@Dao
interface RoomGalleryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: RoomGalleryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(entities: List<RoomGalleryEntity>)

  @Query("SELECT * FROM gallery WHERE id = :id LIMIT 1")
  suspend fun get(id: NasaId): RoomGalleryEntity?

  @Query("DELETE FROM gallery WHERE id = :id")
  suspend fun delete(id: NasaId)
}

class RoomGalleryDaoWrapper @Inject constructor(db: RoomNasaDatabase) : GalleryDao {
  private val delegate = db.galleryDao()
  override suspend fun insert(entity: GalleryEntity) = delegate.insert(entity.toImpl())
  override suspend fun insert(entities: List<GalleryEntity>) = delegate.insertAll(entities.map { it.toImpl() })
  override suspend fun get(id: NasaId) = delegate.get(id)
  override suspend fun delete(id: NasaId) = delegate.delete(id)

  private fun GalleryEntity.toImpl() = if (this is RoomGalleryEntity) {
    this
  } else {
    RoomGalleryEntity(id, collectionUrl, mediaType)
  }
}

val DefaultGalleryEntityFactory = GalleryEntity.Factory(::RoomGalleryEntity)
