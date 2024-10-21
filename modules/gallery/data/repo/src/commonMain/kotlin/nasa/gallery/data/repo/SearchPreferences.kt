package nasa.gallery.data.repo

import dev.jonpoulton.preferences.core.IntSerializer
import dev.jonpoulton.preferences.core.Preference
import dev.jonpoulton.preferences.core.Preferences
import dev.jonpoulton.preferences.core.SimpleNullableStringSerializer
import dev.jonpoulton.preferences.core.enumOrdinalSerializer
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.SearchViewType
import nasa.gallery.model.Year
import javax.inject.Inject

class SearchPreferences @Inject constructor(preferences: Preferences) {
  // Number of results to be returned with each search request
  val pageSize: Preference<Int> = preferences.getInt(key = "search.pageSize", default = 100)

  val yearStart: Preference<Year> = preferences.getObject(
    key = "config.yearStart",
    default = Year.Minimum,
    serializer = YEAR_SERIALIZER,
  )

  val yearEnd: Preference<Year> = preferences.getObject(
    key = "config.yearEnd",
    default = Year.Maximum,
    serializer = YEAR_SERIALIZER,
  )

  val mediaTypes: Preference<MediaTypes?> = preferences.getNullableObject(
    key = "config.mediaTypes",
    default = null,
    serializer = SimpleNullableStringSerializer { string -> string?.let(::MediaTypes) },
  )

  val viewType: Preference<SearchViewType> = preferences.getObject(
    key = "config.viewType",
    default = SearchViewType.Card,
    serializer = enumOrdinalSerializer(),
  )

  val gridColumnWidthDp: Preference<Int> = preferences.getInt(
    key = "config.gridColumnWidthDp",
    default = DEFAULT_GRID_COLUMN_WIDTH,
  )

  companion object {
    const val DEFAULT_GRID_COLUMN_WIDTH = 120

    private val YEAR_SERIALIZER = object : IntSerializer<Year> {
      override fun deserialize(value: Int): Year = Year(value)
      override fun serialize(value: Year): Int = value.value
    }
  }
}
