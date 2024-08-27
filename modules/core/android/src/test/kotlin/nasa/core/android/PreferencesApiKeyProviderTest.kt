package nasa.core.android

import alakazam.test.core.standardDispatcher
import app.cash.turbine.test
import dev.jonpoulton.preferences.core.Preferences
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import nasa.core.model.ApiKey
import nasa.test.buildPreferences
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class PreferencesApiKeyProviderTest {
  @get:Rule
  val timberRule = TimberTestRule.logAllWhenTestFails()!!

  private lateinit var provider: PreferencesApiKeyProvider
  private lateinit var prefs: Preferences

  @Test
  fun `Empty prefs means key is not set`() = runTest {
    buildProvider()
    provider.observe().test {
      assertNull(awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Setting null means key is still not set`() = runTest {
    buildProvider()
    provider.observe().test {
      // Given
      assertNull(awaitItem())

      // When
      provider.set(null)
      testScheduler.advanceUntilIdle()

      // Then
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Setting to non-null means key is set`() = runTest {
    buildProvider()
    provider.observe().test {
      // Given
      assertNull(awaitItem())

      // When
      provider.set(KEY_1)

      // Then
      assertEquals(KEY_1, awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Setting when already set does nothing`() = runTest {
    buildProvider()
    provider.observe().test {
      // Given it's initially empty
      assertNull(awaitItem())

      // When a first key is set
      provider.set(KEY_1)
      assertEquals(KEY_1, awaitItem())

      // and a second key is set
      provider.set(KEY_2)
      testScheduler.advanceUntilIdle()

      // Then the first key is still set - no more keys have been emitted
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun TestScope.buildProvider() {
    prefs = buildPreferences(standardDispatcher)
    provider = PreferencesApiKeyProvider(prefs)
  }

  private companion object {
    val KEY_1 = ApiKey(value = "abcd1234")
    val KEY_2 = ApiKey(value = "1234abcd")
  }
}
