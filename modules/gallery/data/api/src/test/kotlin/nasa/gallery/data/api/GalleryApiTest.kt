package nasa.gallery.data.api

import alakazam.test.core.CoroutineRule
import alakazam.test.core.getResourceAsStream
import kotlinx.coroutines.test.runTest
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.NasaId
import nasa.gallery.model.Year
import nasa.test.http.MockWebServerRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class GalleryApiTest {
  @get:Rule
  val coroutineRule = CoroutineRule()

  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var galleryApi: GalleryApi

  @Before
  fun before() {
    galleryApi = webServerRule.buildApi(json = GalleryJson)
  }

  @Test
  fun `Encode single media type`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(mediaTypes = MediaTypes(MediaType.Video))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "video",
      actual = request.requestUrl?.queryParameter(name = "media_type"),
    )
  }

  @Test
  fun `Encode two media types`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(mediaTypes = MediaTypes(MediaType.Video, MediaType.Audio))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "video,audio",
      actual = request.requestUrl?.queryParameter(name = "media_type"),
    )
  }

  @Test
  fun `Encode three media types`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(mediaTypes = MediaTypes(MediaType.Video, MediaType.Audio, MediaType.Image))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "video,audio,image",
      actual = request.requestUrl?.queryParameter(name = "media_type"),
    )
  }

  @Test
  fun `Encode years`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(
      yearStart = Year(1900),
      yearEnd = Year(2024),
    )

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(expected = "1900", actual = request.requestUrl?.queryParameter(name = "year_start"))
    assertEquals(expected = "2024", actual = request.requestUrl?.queryParameter(name = "year_end"))
  }

  @Test
  fun `Encode NASA ID`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(nasaId = NasaId("abc"))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "abc",
      actual = request.requestUrl?.queryParameter(name = "nasa_id"),
    )
  }

  @Test
  fun `Encode single keyword`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(keywords = Keywords("abc"))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "abc",
      actual = request.requestUrl?.queryParameter(name = "keywords"),
    )
  }

  @Test
  fun `Encode multiple keywords`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(keywords = Keywords("abc", "def", "xyz"))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "abc,def,xyz",
      actual = request.requestUrl?.queryParameter(name = "keywords"),
    )
  }

  @Test
  fun `Encode asset manifest URL`() = runTest {
    enqueueDefault()

    // When
    val id = "abc"
    galleryApi.getAssetManifest(NasaId(id))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "/asset/abc",
      actual = request.requestUrl?.encodedPath,
    )
  }

  @Test
  fun `Encode metadata URL`() = runTest {
    enqueueDefault()

    // When
    val id = "abc"
    galleryApi.locateMetadata(NasaId(id))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "/metadata/abc",
      actual = request.requestUrl?.encodedPath,
    )
  }

  @Test
  fun `Encode captions URL`() = runTest {
    enqueueDefault()

    // When
    val id = "abc"
    galleryApi.locateCaptions(NasaId(id))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "/captions/abc",
      actual = request.requestUrl?.encodedPath,
    )
  }

  @Test
  fun `Get collection`() = runTest {
    // Given
    val collectionJson = readJson(filename = "collection-success.json")
    webServerRule.enqueue(collectionJson)

    // When
    val response = galleryApi.getCollection(url = "url.com")

    // Then
    assertTrue(response.isSuccessful)
    assertEquals(
      expected = listOf(
        "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~orig.jpg",
        "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~orig.jpg",
        "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~large.jpg",
        "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~medium.jpg",
        "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~small.jpg",
        "http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~thumb.jpg",
        "http://images-assets.nasa.gov/image/NHQ202401260011/metadata.json",
      ),
      actual = response.body(),
    )
  }

  @Test
  fun `Get metadata`() = runTest {
    // Given
    val collectionJson = readJson(filename = "metadata-success.json")
    webServerRule.enqueue(collectionJson)

    // When
    val response = galleryApi.getMetadata(url = "url.com")

    // Then
    assertTrue(response.isSuccessful)
    val body = response.body() ?: fail("Null body for $response")
    assertEquals(expected = 349, actual = body.size)
  }

  private fun enqueueDefault() {
    val errorJson = readJson(filename = "search-failure.json")
    webServerRule.enqueue(errorJson, code = 404)
  }

  private fun readJson(filename: String): String = getResourceAsStream(filename).reader().readText()
}
