package nasa.gallery.data.api

import alakazam.test.core.getResourceAsStream
import org.junit.Test
import kotlin.test.assertEquals

class LocateResponseTest {
  @Test
  fun `Deserialize failure`() {
    val json = getJson(filename = "locate-failure.json")
    val response = GalleryJson.decodeFromString<LocateResponse>(json)
    assertEquals(
      expected = LocateResponse.Failure(reason = "No AssetDB records for nasaid=as11-40-587"),
      actual = response,
    )
  }

  @Test
  fun `Deserialize success`() {
    val json = getJson(filename = "locate-success.json")
    val response = GalleryJson.decodeFromString<LocateResponse>(json)
    assertEquals(
      expected = LocateResponse.Success("https://images-assets.nasa.gov/image/as11-40-5874/metadata.json"),
      actual = response,
    )
  }

  private fun getJson(filename: String): String = getResourceAsStream(filename).reader().readText()
}
