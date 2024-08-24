package nasa.settings.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import nasa.core.model.bytes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class ImageCacheTest {
  private lateinit var context: Context
  private lateinit var imageCache: ImageCache

  @Before
  fun before() {
    context = ApplicationProvider.getApplicationContext()
    imageCache = ImageCache(context)
    imageCache.cacheDir.mkdirs()
  }

  @Test
  fun `Calculate size`() = runTest {
    // Given
    imageCache.writeBytesToFile(filename = "a.txt", numBytes = 123)
    imageCache.writeBytesToFile(filename = "b.json", numBytes = 234)
    imageCache.writeBytesToFile(filename = "c.exe", numBytes = 345)

    // When
    val size = imageCache.calculateSize()

    // Then
    assertEquals(expected = 702.bytes, actual = size)

    // When we delete it all
    imageCache.clear()
    val size2 = imageCache.calculateSize()

    // Then it's back to zero
    assertEquals(expected = 0.bytes, actual = size2)
  }

  @Test
  fun `Calculate size when deleted`() = runTest {
    // Given the folder doesn't exist
    assertTrue(imageCache.cacheDir.delete())
    assertFalse(imageCache.cacheDir.exists())

    // When
    imageCache.clear()
    val size = imageCache.calculateSize()

    // Then
    assertEquals(expected = 0.bytes, actual = size)
  }

  @Test
  fun `Calculate size for empty folder`() = runTest {
    // Given nothing is in the folder
    assertTrue(imageCache.cacheDir.exists())
    assertEquals(expected = 0, actual = imageCache.cacheDir.listFiles()?.size)

    // When
    val size = imageCache.calculateSize()

    // Then
    assertEquals(expected = 0.bytes, actual = size)
  }
}
