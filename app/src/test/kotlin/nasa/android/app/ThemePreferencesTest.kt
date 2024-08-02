package nasa.android.app

import alakazam.test.core.CoroutineRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import nasa.core.model.ThemeType
import nasa.test.buildPreferences
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class ThemePreferencesTest {
  @get:Rule
  val coroutineRule = CoroutineRule()

  private lateinit var preferences: ThemePreferences

  @Before
  fun before() {
    val prefs = buildPreferences(coroutineRule.dispatcher)
    preferences = ThemePreferences(prefs)
  }

  @Test
  fun `Get default value`() = runTest {
    assertEquals(preferences.theme.get(), ThemeType.System)
  }

  @Test
  fun `Change value`() = runTest {
    preferences.theme.asFlow().test {
      // Initial state
      assertEquals(ThemeType.System, awaitItem())

      // Change to all possible values
      preferences.theme.set(ThemeType.Light)
      assertEquals(ThemeType.Light, awaitItem())

      preferences.theme.set(ThemeType.Dark)
      assertEquals(ThemeType.Dark, awaitItem())

      preferences.theme.set(ThemeType.Midnight)
      assertEquals(ThemeType.Midnight, awaitItem())

      // Back to default
      preferences.theme.delete()
      assertEquals(ThemeType.System, awaitItem())
    }
  }
}
