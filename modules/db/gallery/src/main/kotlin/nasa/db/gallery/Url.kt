package nasa.db.gallery

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nasa.gallery.model.NasaId
import nasa.gallery.model.UrlCollection

@Entity(
  tableName = "url",
  indices = [
    Index(value = ["galleryId"]),
  ],
  foreignKeys = [
    ForeignKey(
      entity = GalleryEntity::class,
      parentColumns = ["id"],
      childColumns = ["galleryId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class UrlEntity(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long = 0L,

  @ColumnInfo(name = "galleryId")
  val galleryId: NasaId,

  @ColumnInfo(name = "urls")
  val urls: UrlCollection,
)

@Dao
interface UrlDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: UrlEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<UrlEntity>)

  @Query("SELECT * FROM url WHERE galleryId = :id LIMIT 1")
  suspend fun get(id: NasaId): UrlEntity?

  @Query("SELECT * FROM url")
  fun observe(): Flow<List<UrlEntity>>
}
