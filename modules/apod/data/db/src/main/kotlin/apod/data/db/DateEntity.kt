package apod.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

/**
 * Used to track knowledge of specific dates in the APOD archive where no image was published, for whatever reason.
 * If we make a request for a given date and a 404 error is returned.
 */
@Entity(tableName = "date")
data class DateEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("date")
  val date: LocalDate,

  @ColumnInfo("exists")
  val exists: Boolean,
)
