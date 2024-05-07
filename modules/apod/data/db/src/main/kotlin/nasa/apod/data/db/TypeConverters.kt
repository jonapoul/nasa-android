package nasa.apod.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodMediaType

internal object ApodMediaTypeConverter {
  @TypeConverter
  fun toString(type: ApodMediaType): String = type.name

  @TypeConverter
  fun fromString(string: String): ApodMediaType = ApodMediaType.valueOf(string)
}

internal object LocalDateTypeConverter {
  @TypeConverter
  fun toString(date: LocalDate): String = date.toString()

  @TypeConverter
  fun fromString(string: String): LocalDate = LocalDate.parse(string)
}
