package nasa.android.app

import alakazam.test.core.CoroutineRule
import app.cash.turbine.test
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.test.runTest
import nasa.core.model.ApiKey
import nasa.test.prefs.buildFlowSharedPreferences
import nasa.test.prefs.buildSharedPreferences
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class ApiKeyManagerTest {
  @get:Rule
  val coroutineRule = CoroutineRule()

  @get:Rule
  val timberRule = TimberTestRule.logAllWhenTestFails()!!

  private lateinit var apiKeyManager: ApiKeyManager
  private lateinit var flowPrefs: FlowSharedPreferences

  @Before
  fun before() {
    val sharedPrefs = buildSharedPreferences(name = "test-prefs")
    flowPrefs = buildFlowSharedPreferences(coroutineRule.dispatcher, sharedPrefs)
    apiKeyManager = ApiKeyManager(flowPrefs)
  }

  @Test
  fun `Empty prefs means key is not set`() = runTest {
    apiKeyManager.observe().test {
      assertNull(awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Setting null means key is still not set`() = runTest {
    apiKeyManager.observe().test {
      // Given
      assertNull(awaitItem())

      // When
      apiKeyManager.set(null)
      testScheduler.advanceUntilIdle()

      // Then
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Setting to non-null means key is set`() = runTest {
    apiKeyManager.observe().test {
      // Given
      assertNull(awaitItem())

      // When
      apiKeyManager.set(KEY_1)

      // Then
      assertEquals(KEY_1, awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Setting when already set does nothing`() = runTest {
    apiKeyManager.observe().test {
      // Given it's initially empty
      assertNull(awaitItem())

      // When a first key is set
      apiKeyManager.set(KEY_1)
      assertEquals(KEY_1, awaitItem())

      // and a second key is set
      apiKeyManager.set(KEY_2)
      testScheduler.advanceUntilIdle()

      // Then the first key is still set - no more keys have been emitted
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  private companion object {
    val KEY_1 = ApiKey(value = "abcd1234")
    val KEY_2 = ApiKey(value = "1234abcd")
  }
}
