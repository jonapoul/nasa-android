package nasa.db.gallery

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import nasa.gallery.model.Album

@Entity(tableName = "album")
data class AlbumEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("album")
  val album: Album,
)

@Dao
interface AlbumDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: AlbumEntity)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<AlbumEntity>)
}
