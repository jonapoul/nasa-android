package nasa.db.gallery

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId

@Entity(tableName = "gallery")
data class GalleryEntity(
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: NasaId,

  @ColumnInfo(name = "collectionUrl", defaultValue = "")
  val collectionUrl: JsonUrl,

  @ColumnInfo(name = "mediaType", defaultValue = "image")
  val mediaType: MediaType,
)

@Dao
interface GalleryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: GalleryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<GalleryEntity>)

  @Query("SELECT * FROM gallery WHERE id = :id LIMIT 1")
  suspend fun get(id: NasaId): GalleryEntity?

  @Query("DELETE FROM gallery WHERE id = :id")
  suspend fun delete(id: NasaId)
}
