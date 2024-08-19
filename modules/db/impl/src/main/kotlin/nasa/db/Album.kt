package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.db.gallery.AlbumDao
import nasa.gallery.model.Album
import javax.inject.Inject

@Entity(tableName = "album")
data class RoomAlbumEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("album")
  val album: Album,
)

@Dao
interface RoomAlbumDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: RoomAlbumEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<RoomAlbumEntity>)
}

class RoomAlbumDaoWrapper @Inject constructor(db: RoomNasaDatabase) : AlbumDao {
  private val delegate = db.albumDao()
  override suspend fun insert(album: Album) = delegate.insert(RoomAlbumEntity(album))
  override suspend fun insert(albums: List<Album>) = delegate.insert(albums.map(::RoomAlbumEntity))
}
