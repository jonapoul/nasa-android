package nasa.core.db

import androidx.room.TypeConverter
import nasa.apod.model.ApodMediaType

internal object ApodMediaTypeConverter {
  @TypeConverter
  fun toString(type: ApodMediaType): String = type.name

  @TypeConverter
  fun fromString(string: String): ApodMediaType = ApodMediaType.valueOf(string)
}
