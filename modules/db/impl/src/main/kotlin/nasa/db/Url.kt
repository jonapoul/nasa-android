package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import nasa.db.gallery.UrlDao
import nasa.gallery.model.NasaId
import nasa.gallery.model.UrlCollection
import javax.inject.Inject

@Entity(
  tableName = "url",
  indices = [
    Index(value = ["galleryId"]),
  ],
  foreignKeys = [
    ForeignKey(
      entity = RoomGalleryEntity::class,
      parentColumns = ["id"],
      childColumns = ["galleryId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
)
data class RoomUrlEntity(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long = 0L,

  @ColumnInfo(name = "galleryId")
  val galleryId: NasaId,

  @ColumnInfo(name = "urls")
  val urls: UrlCollection,
)

@Dao
interface RoomUrlCollectionDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: RoomUrlEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<RoomUrlEntity>)

  @Query("SELECT * FROM url WHERE galleryId = :id LIMIT 1")
  suspend fun get(id: NasaId): RoomUrlEntity?

  @Query("SELECT * FROM url")
  fun observe(): Flow<List<RoomUrlEntity>>
}

class RoomUrlDaoWrapper @Inject constructor(db: RoomNasaDatabase) : UrlDao {
  private val delegate = db.urlDao()
  override suspend fun insert(id: NasaId, urls: UrlCollection) = delegate.insert(RoomUrlEntity(0L, id, urls))
  override suspend fun get(id: NasaId) = delegate.get(id)?.urls
  override fun observe() = delegate.observe().map { entities -> entities.map { it.urls } }
}

internal object UrlCollectionTypeConverter {
  @TypeConverter
  fun toString(data: UrlCollection): String = Json.encodeToString(UrlCollection.serializer(), data)

  @TypeConverter
  fun fromString(string: String): UrlCollection = Json.decodeFromString(UrlCollection.serializer(), string)
}
