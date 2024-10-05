package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
