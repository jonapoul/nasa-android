package nasa.apod.data.api

import alakazam.test.core.CoroutineRule
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodMediaType
import nasa.core.model.ApiKey
import nasa.test.MockWebServerRule
import nasa.test.getResourceAsText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

class ApodApiTest {
  @get:Rule
  val coroutineRule = CoroutineRule()

  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var apodApi: ApodApi

  @Before
  fun before() {
    apodApi = webServerRule.buildApi(json = ApodJson)
  }

  @Test
  fun `No API key gives failure`() = runTest {
    // Given
    val errorJson = readJson(filename = "no-api-key.json")
    webServerRule.enqueue(errorJson, code = 404)

    // When
    val response = apodApi.getToday(apiKey = ApiKey(API_KEY))

    // Then the response was in the error body, not the regular body
    assertEquals(actual = response.errorBody()?.string(), expected = errorJson)
    assertNull(response.body())
  }

  @Test
  fun `Fetch valid single item`() = runTest {
    // Given
    val json = readJson(filename = "valid-single.json")
    webServerRule.enqueue(json)

    // When
    val response = apodApi.getToday(apiKey = ApiKey(API_KEY))

    // Then the response was deserialized properly
    assertEquals(actual = response.body(), expected = EXAMPLE_APOD_ITEM)

    // And the request URL/params were encoded properly
    val url = webServerRule.server.takeRequest().requestUrl ?: fail("No request URL?")
    assertEquals(actual = url.queryParameterNames, expected = setOf("api_key", "thumbs"))
    assertEquals(actual = url.queryParameter(name = "api_key"), expected = API_KEY)
    assertEquals(actual = url.queryParameter(name = "thumbs"), expected = "true")
  }

  @Test
  fun `Fetch specific date`() = runTest {
    // Given
    val json = readJson(filename = "valid-single.json")
    webServerRule.enqueue(json)

    // When
    val date = "2024-04-29"
    val response = apodApi.getByDate(
      apiKey = ApiKey(API_KEY),
      date = LocalDate.parse(date),
    )

    // Then the response was deserialized properly
    assertEquals(actual = response.body(), expected = EXAMPLE_APOD_ITEM)

    // And the request URL/params were encoded properly
    val url = webServerRule.server.takeRequest().requestUrl ?: fail("No request URL?")
    assertEquals(actual = url.queryParameterNames, expected = setOf("api_key", "date", "thumbs"))
    assertEquals(actual = url.queryParameter(name = "api_key"), expected = API_KEY)
    assertEquals(actual = url.queryParameter(name = "thumbs"), expected = "true")

    // also including a date
    assertEquals(actual = url.queryParameter(name = "date"), expected = date)
  }

  private fun readJson(filename: String): String = getResourceAsText(filename)

  private companion object {
    const val API_KEY = "SOME_DUMMY_API_KEY"

    private val EXAMPLE_APOD_ITEM = ApodResponseModel(
      title = "GK Per: Nova and Planetary Nebula",
      date = LocalDate.parse("2024-04-30"),
      mediaType = ApodMediaType.Image,
      thumbnailUrl = null,
      copyright = "\nDeep Sky Collective\n",
      url = "https://apod.nasa.gov/apod/image/2404/GKPerWide_DSC_960.jpg",
      hdUrl = "https://apod.nasa.gov/apod/image/2404/GKPerWide_DSC_4329.jpg",
      explanation = "Here's a dummy explanation",
    )
  }
}
