package apod.nav

import android.os.Parcel
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

// readSerializable is deprecated, but we need it!
@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class ScreenConfigTest {
  private lateinit var parcel: Parcel

  @Before
  fun before() {
    parcel = Parcel.obtain()
  }

  @After
  fun after() {
    parcel.recycle()
  }

  @Test
  fun `Serialize today`() {
    parcel.writeSerializable(ScreenConfig.Today)
    parcel.setDataPosition(0)

    val config = parcel.readSerializable() as? ScreenConfig?
    assertEquals(expected = ScreenConfig.Today, actual = config)
  }

  @Test
  fun `Serialize random`() {
    parcel.writeSerializable(ScreenConfig.Random)
    parcel.setDataPosition(0)

    val config = parcel.readSerializable() as? ScreenConfig?
    assertEquals(expected = ScreenConfig.Random, actual = config)
  }

  @Test
  fun `Serialize specific`() {
    val date = LocalDate.parse("2024-05-05")
    parcel.writeSerializable(ScreenConfig.Specific(date))
    parcel.setDataPosition(0)

    val config = parcel.readSerializable() as? ScreenConfig?
    assertEquals(expected = ScreenConfig.Specific(date), actual = config)
  }
}
