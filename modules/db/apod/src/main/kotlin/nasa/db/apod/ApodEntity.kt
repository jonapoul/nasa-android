package nasa.db.apod

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "apod")
data class ApodEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("date")
  val date: LocalDate,

  @ColumnInfo("title")
  val title: String,

  @ColumnInfo("explanation")
  val explanation: String,

  @ColumnInfo("media_type")
  val mediaType: String,

  @ColumnInfo("copyright")
  val copyright: String?,

  @ColumnInfo("url")
  val url: String?,

  @ColumnInfo("hdurl")
  val hdUrl: String?,

  @ColumnInfo("thumbnail_url")
  val thumbnailUrl: String?,
)
