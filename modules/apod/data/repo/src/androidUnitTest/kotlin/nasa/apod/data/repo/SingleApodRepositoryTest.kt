package nasa.apod.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.test.core.standardDispatcher
import alakazam.test.db.RoomDatabaseRule
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import nasa.apod.data.api.ApodApi
import nasa.apod.data.api.ApodJson
import nasa.apod.model.ApodItem
import nasa.apod.model.ApodMediaType
import nasa.core.model.ApiKey
import nasa.db.ApodDao
import nasa.db.NasaDatabase
import nasa.test.MockWebServerRule
import nasa.test.getResourceAsText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class SingleApodRepositoryTest {
  @get:Rule
  val databaseRule = RoomDatabaseRule(NasaDatabase::class)

  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var repository: SingleApodRepository
  private lateinit var dao: ApodDao
  private lateinit var api: ApodApi

  private fun TestScope.before() {
    dao = databaseRule.database.apodDao()
    api = webServerRule.buildApi(json = ApodJson)

    repository = SingleApodRepository(
      io = IODispatcher(standardDispatcher),
      api = api,
      dao = dao,
      calendar = { TODAY },
      sharedRepository = SharedRepository(),
    )
  }

  @Test
  fun `Fetch today and store in database`() = runTest {
    // Given a valid response is returned from the server
    before()
    webServerRule.enqueue(
      code = 200,
      body = getResourceAsText(filename = "single-valid-with-newlines.json"),
    )

    // and the database doesn't have anything
    assertNull(dao.get(DATE))

    // When we query for the latest from the API
    val result = repository.loadToday(API_KEY)

    // Then the item is parsed and returned
    assertEquals(
      expected = SingleLoadResult.Success(ITEM),
      actual = result,
    )

    // and the API was queried once
    webServerRule.assertRequestCount(expected = 1)
    val request = webServerRule.server.takeRequest()

    // and the request URL didn't contain a specific date
    assertEquals(expected = false, actual = request.requestUrl?.queryParameterNames?.contains("date"))

    // and the database has a new entry on the given date
    assertNotNull(dao.get(DATE))
  }

  @Test
  fun `Don't query API if it's cached locally`() = runTest {
    // Given the database has an entry for the specified date
    before()
    val entity = ITEM.toEntity()
    dao.insert(entity)

    // When
    val result = repository.loadSpecific(API_KEY, DATE)

    // Then the item is returned
    assertEquals(expected = SingleLoadResult.Success(ITEM), actual = result)

    // but the API wasn't queried
    webServerRule.assertRequestCount(expected = 0)
  }

  @Test
  fun `Requesting out of valid range`() = runTest {
    // Given an error response is returned from the server
    before()
    webServerRule.enqueue(
      code = 400,
      body = getResourceAsText(filename = "out-of-range.json"),
    )

    // When we query the API
    val invalidDate = LocalDate.parse("1994-01-01")
    val result = repository.loadSpecific(API_KEY, invalidDate)

    // Then a failure is returned
    assertIs<FailureResult.OutOfRange>(result)
  }

  @Test
  fun `Requesting with an invalid API key`() = runTest {
    // Given an error response is returned from the server
    before()
    webServerRule.enqueue(
      code = 403,
      body = getResourceAsText(filename = "invalid-api-key.json"),
    )

    // and our key is dodgy
    val dodgyKey = ApiKey(value = "this is not a valid key")

    // When we query the API
    val result = repository.loadSpecific(dodgyKey, DATE)

    // Then a failure is returned
    assertIs<FailureResult.InvalidAuth>(result)
  }

  @Test
  fun `Handle unexpected JSON response format`() = runTest {
    // Given an success response is returned from the server, but not in an expected JSON structure
    before()
    webServerRule.enqueue(code = 200, body = getResourceAsText(filename = "invalid-response-format.json"))

    // When we query the API
    val result = repository.loadSpecific(API_KEY, DATE)

    // Then a failure is returned
    assertIs<FailureResult.Json>(result)
  }

  @Test
  fun `Handle date without an APOD item`() = runTest {
    // Given APOD doesn't have an item for the requested date
    before()
    webServerRule.enqueue(code = 404, body = getResourceAsText(filename = "nonexistent-date.json"))

    // When we query the API
    val result = repository.loadSpecific(API_KEY, DATE)

    // Then a failure is returned
    assertEquals(expected = FailureResult.NoApod(DATE), actual = result)
  }

  @Test
  fun `Handle network problems`() = runTest {
    // Given the mock server isn't running
    before()
    webServerRule.server.shutdown()

    // When we query the API
    val result = repository.loadSpecific(API_KEY, DATE)

    // Then a failure is returned
    assertEquals(expected = FailureResult.Network, actual = result)
  }

  @Test
  fun `Handle other HTTP code`() = runTest {
    // Given the server returns an unexpected code
    before()
    webServerRule.enqueue(code = 405, body = getResourceAsText(filename = "other-http.json"))

    // When we query the API
    val result = repository.loadSpecific(API_KEY, DATE)

    // Then a failure is returned
    assertEquals(
      expected = FailureResult.OtherHttp(code = 405, message = "Some other problem"),
      actual = result,
    )
  }

  @Test
  fun `Fetch random item`() = runTest {
    // Given the server returns a successful random response
    before()
    webServerRule.enqueue(code = 200, body = getResourceAsText(filename = "single-valid-random.json"))

    // When we query the API
    val result = repository.loadRandom(API_KEY)

    // Then a success is returned
    val date = LocalDate.parse("2006-04-05")
    val randomItem = ApodItem(
      date = date,
      title = "Slightly Beneath Saturn's Ring Plane",
      explanation = "Shortened explanation for easier testing",
      mediaType = ApodMediaType.Image,
      copyright = null,
      url = "https://apod.nasa.gov/apod/image/0604/rhearings_cassini.jpg",
      hdUrl = "https://apod.nasa.gov/apod/image/0604/rhearings_cassini_big.jpg",
      thumbnailUrl = null,
    )
    assertEquals(expected = SingleLoadResult.Success(randomItem), actual = result)

    // and the database has an entry on the given date
    assertNotNull(dao.get(date))
  }

  private companion object {
    val API_KEY = ApiKey(value = "SOME_DUMMY_KEY")

    val TODAY = LocalDate.parse("2024-05-01")
    val DATE = LocalDate.parse("2024-04-30")

    val ITEM = ApodItem(
      title = "GK Per: Nova and Planetary Nebula",
      date = DATE,
      mediaType = ApodMediaType.Image,
      thumbnailUrl = null,
      copyright = "Deep Sky Collective",
      url = "https://apod.nasa.gov/apod/image/2404/GKPerWide_DSC_960.jpg",
      hdUrl = "https://apod.nasa.gov/apod/image/2404/GKPerWide_DSC_4329.jpg",
      explanation = "Here's a dummy explanation",
    )
  }
}
