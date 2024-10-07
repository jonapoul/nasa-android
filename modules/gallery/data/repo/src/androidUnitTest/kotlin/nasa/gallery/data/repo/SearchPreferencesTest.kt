package nasa.gallery.data.repo

import alakazam.test.core.standardDispatcher
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import nasa.gallery.model.year
import nasa.test.buildPreferences
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
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
      assertEquals(expected = Year.Minimum, actual = awaitItem())

      val year = 1234.year
      searchPreferences.yearStart.set(year)
      assertEquals(expected = year, actual = awaitItem())

      searchPreferences.yearStart.delete()
      assertEquals(expected = Year.Minimum, actual = awaitItem())

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Media types`() = runTest {
    before()
    searchPreferences.mediaTypes.asFlow().test {
      assertEquals(expected = null, actual = awaitItem())

      searchPreferences.mediaTypes.set(MediaTypes.Empty)
      assertEquals(expected = MediaTypes.Empty, actual = awaitItem())

      searchPreferences.mediaTypes.set(MediaTypes.All)
      assertEquals(expected = MediaTypes.All, actual = awaitItem())

      val types = MediaTypes(MediaType.Audio, MediaType.Image)
      searchPreferences.mediaTypes.set(types)
      assertEquals(expected = types, actual = awaitItem())

      searchPreferences.mediaTypes.delete()
      assertEquals(expected = null, actual = awaitItem())

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }
}
