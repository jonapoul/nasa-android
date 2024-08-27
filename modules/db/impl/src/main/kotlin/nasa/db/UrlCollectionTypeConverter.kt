package nasa.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import nasa.gallery.model.UrlCollection

internal object UrlCollectionTypeConverter {
  @TypeConverter
  fun toString(data: UrlCollection): String = Json.encodeToString(UrlCollection.serializer(), data)

  @TypeConverter
  fun fromString(string: String): UrlCollection = Json.decodeFromString(UrlCollection.serializer(), string)
}
