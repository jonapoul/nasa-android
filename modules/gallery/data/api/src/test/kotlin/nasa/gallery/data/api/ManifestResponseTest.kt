package nasa.gallery.data.api

import alakazam.test.core.getResourceAsStream
import org.junit.Test
import kotlin.test.assertEquals

class ManifestResponseTest {
  @Test
  fun `Deserialize failure`() {
    val json = getJson(filename = "manifest-failure.json")
    val response = GalleryJson.decodeFromString<ManifestResponse>(json)
    assertEquals(
      expected = ManifestResponse.Failure(reason = "No AssetDB records for nasaid=as11-40-584"),
      actual = response,
    )
  }

  @Test
  fun `Deserialize success`() {
    val json = getJson(filename = "manifest-success.json")
    val response = GalleryJson.decodeFromString<ManifestResponse>(json)
    assertEquals(
      expected = ManifestResponse.Success(
        collection = ManifestCollection(
          version = "1.0",
          url = "http://images-api.nasa.gov/asset/as11-40-5874",
          items = listOf(
            ManifestItem(url = "http://images-assets.nasa.gov/image/as11-40-5874/as11-40-5874~orig.jpg"),
            ManifestItem(url = "http://images-assets.nasa.gov/image/as11-40-5874/as11-40-5874~large.jpg"),
            ManifestItem(url = "http://images-assets.nasa.gov/image/as11-40-5874/as11-40-5874~medium.jpg"),
            ManifestItem(url = "http://images-assets.nasa.gov/image/as11-40-5874/as11-40-5874~small.jpg"),
            ManifestItem(url = "http://images-assets.nasa.gov/image/as11-40-5874/as11-40-5874~thumb.jpg"),
            ManifestItem(url = "http://images-assets.nasa.gov/image/as11-40-5874/metadata.json"),
          ),
        ),
      ),
      actual = response,
    )
  }

  private fun getJson(filename: String): String = getResourceAsStream(filename).reader().readText()
}
