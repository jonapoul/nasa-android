package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import nasa.db.gallery.AlbumDao
import nasa.gallery.model.Album

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

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<RoomAlbumEntity>)

  @Query("SELECT * FROM album ORDER BY album DESC")
  suspend fun getAll(): List<RoomAlbumEntity>

  @Query("DELETE FROM album")
  suspend fun clear()
}

class RoomAlbumDaoWrapper(private val delegate: RoomAlbumDao) : AlbumDao {
  override suspend fun insert(album: Album) = delegate.insert(RoomAlbumEntity(album))
  override suspend fun insertAll(albums: List<Album>) = delegate.insertAll(albums.map(::RoomAlbumEntity))
  override suspend fun getAll() = delegate.getAll().map { it.album }
  override suspend fun clear() = delegate.clear()
}
