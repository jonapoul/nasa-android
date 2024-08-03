package nasa.gallery.data.repo

import dev.jonpoulton.preferences.core.Preference
import dev.jonpoulton.preferences.core.Preferences
import javax.inject.Inject

internal class SearchPreferences @Inject constructor(preferences: Preferences) {
  // Number of results to be returned with each search request
  val pageSize: Preference<Int> = preferences.getInt(key = "search.pageSize", default = 100)
}
