package nasa.android.app

import alakazam.test.core.standardDispatcher
import app.cash.turbine.test
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import nasa.core.model.ThemeType
import nasa.test.assertEmitted
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
      assertEmitted(ThemeType.System)

      // Change to all possible values
      preferences.theme.set(ThemeType.Light)
      assertEmitted(ThemeType.Light)

      preferences.theme.set(ThemeType.Dark)
      assertEmitted(ThemeType.Dark)

      preferences.theme.set(ThemeType.Midnight)
      assertEmitted(ThemeType.Midnight)

      // Back to default
      preferences.theme.delete()
      assertEmitted(ThemeType.System)
    }
  }

  private fun TestScope.buildPreferences() {
    val prefs = buildPreferences(standardDispatcher)
    preferences = ThemePreferences(prefs)
  }
}
