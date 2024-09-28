package nasa.gallery.model

import kotlinx.coroutines.test.runTest
import nasa.gallery.model.MediaType.Audio
import nasa.gallery.model.MediaType.Image
import nasa.gallery.model.MediaType.Video
import org.junit.Test
import kotlin.test.assertEquals

class MediaTypesTest {
  @Test
  fun `Empty types`() = runTest {
    val types = MediaTypes.Empty
    assertEquals(expected = emptySet(), actual = types.toSet())
  }

  @Test
  fun `All types`() = runTest {
    val types = MediaTypes.All
    assertEquals(expected = setOf(Audio, Image, Video), actual = types.toSet())
  }
}
