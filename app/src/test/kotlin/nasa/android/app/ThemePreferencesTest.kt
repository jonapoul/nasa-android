package nasa.android.app

import alakazam.test.core.standardDispatcher
import app.cash.turbine.test
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import nasa.core.model.ThemeType
import nasa.test.buildPreferences
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class ThemePreferencesTest {
  private lateinit var preferences: ThemePreferences

  @Test
  fun `Get default value`() = runTest {
    buildPreferences()
    assertEquals(preferences.theme.get(), ThemeType.System)
  }

  @Test
  fun `Change value`() = runTest {
    buildPreferences()
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

  private fun TestScope.buildPreferences() {
    val prefs = buildPreferences(standardDispatcher)
    preferences = ThemePreferences(prefs)
  }
}
