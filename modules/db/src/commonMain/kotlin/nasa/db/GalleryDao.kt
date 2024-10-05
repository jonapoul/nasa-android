package nasa.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nasa.gallery.model.NasaId

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
