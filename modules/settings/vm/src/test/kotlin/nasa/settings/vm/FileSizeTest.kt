package nasa.settings.vm

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FileSizeTest {
  @Test
  fun `toString B`() = testToString(123.bytes, expected = "123 B")

  @Test
  fun `toString KB`() = testToString(123.kilobytes, expected = "123.0 kB")

  @Test
  fun `toString MB`() = testToString(123.megabytes, expected = "123.0 MB")

  @Test
  fun `toString GB`() = testToString(123_456.megabytes, expected = "123.5 GB")

  @Test
  fun `Compare bytes 1`() = assertTrue(123.bytes < 124.bytes)

  @Test
  fun `Compare bytes 2`() = assertTrue(123.bytes <= 123.bytes)

  @Test
  fun `Compare bytes 3`() = assertTrue(123.kilobytes >= 123.bytes)

  @Test
  fun `Add 1`() = assertEquals(123.bytes, 0.bytes + 123.bytes)

  @Test
  fun `Add 2`() = assertEquals(123.gigabytes, 122.5.gigabytes + 500.megabytes)

  @Test
  fun `Add 3`() = assertEquals(123.kilobytes, 100.kilobytes + 23.kilobytes)

  private fun testToString(input: FileSize, expected: String) = assertEquals(expected, input.toString())
}
