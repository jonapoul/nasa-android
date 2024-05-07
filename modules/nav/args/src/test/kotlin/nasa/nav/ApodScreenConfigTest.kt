package nasa.nav

import android.os.Parcel
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs

// readSerializable is deprecated, but we need it!
@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class ApodScreenConfigTest {
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
    parcel.writeSerializable(ApodScreenConfig.Today)
    parcel.setDataPosition(0)

    val config = parcel.readSerializable() as? ApodScreenConfig?
    assertEquals(expected = ApodScreenConfig.Today, actual = config)
  }

  @Test
  fun `Serialize random`() {
    parcel.writeSerializable(ApodScreenConfig.Random())
    parcel.setDataPosition(0)

    val config = parcel.readSerializable() as? ApodScreenConfig?
    assertIs<ApodScreenConfig.Random>(config)
  }

  @Test
  fun `Serialize specific`() {
    val date = LocalDate.parse("2024-05-05")
    parcel.writeSerializable(ApodScreenConfig.Specific(date))
    parcel.setDataPosition(0)

    val config = parcel.readSerializable() as? ApodScreenConfig?
    assertEquals(expected = ApodScreenConfig.Specific(date), actual = config)
  }
}
