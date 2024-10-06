package nasa.gallery.data.repo

import dev.jonpoulton.preferences.core.Preference
import dev.jonpoulton.preferences.core.Preferences
import dev.jonpoulton.preferences.core.SimpleNullableStringSerializer
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import javax.inject.Inject

class SearchPreferences @Inject constructor(preferences: Preferences) {
  // Number of results to be returned with each search request
  val pageSize: Preference<Int> = preferences.getInt(key = "search.pageSize", default = 100)

  val yearStart: Preference<Year?> = preferences.getNullableObject(
    key = "config.yearStart",
    default = null,
    serializer = YEAR_SERIALIZER,
  )

  val yearEnd: Preference<Year?> = preferences.getNullableObject(
    key = "config.yearEnd",
    default = null,
    serializer = YEAR_SERIALIZER,
  )

  val mediaTypes: Preference<MediaTypes?> = preferences.getNullableObject(
    key = "config.mediaTypes",
    default = null,
    serializer = SimpleNullableStringSerializer { string -> string?.let(::MediaTypes) },
  )

  private companion object {
    val YEAR_SERIALIZER = SimpleNullableStringSerializer { string -> string?.toIntOrNull()?.let(::Year) }
  }
}
