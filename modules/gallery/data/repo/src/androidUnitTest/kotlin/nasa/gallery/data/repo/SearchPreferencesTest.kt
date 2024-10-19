package nasa.gallery.data.repo

import alakazam.test.core.standardDispatcher
import app.cash.turbine.test
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import nasa.gallery.model.year
import nasa.test.assertEmitted
import nasa.test.buildPreferences
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchPreferencesTest {
  private lateinit var searchPreferences: SearchPreferences

  private fun TestScope.before() {
    val prefs = buildPreferences(standardDispatcher)
    searchPreferences = SearchPreferences(prefs)
  }

  @Test
  fun `Year start`() = runTest {
    before()
    searchPreferences.yearStart.asFlow().test {
      assertEmitted(Year.Minimum)

      val year = 1234.year
      searchPreferences.yearStart.set(year)
      assertEmitted(year)

      searchPreferences.yearStart.delete()
      assertEmitted(Year.Minimum)

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Media types`() = runTest {
    before()
    searchPreferences.mediaTypes.asFlow().test {
      assertEmitted(null)

      searchPreferences.mediaTypes.set(MediaTypes.Empty)
      assertEmitted(MediaTypes.Empty)

      searchPreferences.mediaTypes.set(MediaTypes.All)
      assertEmitted(MediaTypes.All)

      val types = MediaTypes(MediaType.Audio, MediaType.Image)
      searchPreferences.mediaTypes.set(types)
      assertEmitted(types)

      searchPreferences.mediaTypes.delete()
      assertEmitted(null)

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }
}
