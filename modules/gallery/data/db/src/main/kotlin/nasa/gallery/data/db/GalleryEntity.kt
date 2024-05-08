package nasa.gallery.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "gallery")
data class GalleryEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("date")
  val date: LocalDate,
)
