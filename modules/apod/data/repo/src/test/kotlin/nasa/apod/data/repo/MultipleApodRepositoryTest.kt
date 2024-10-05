package nasa.apod.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.test.core.standardDispatcher
import alakazam.test.db.RoomDatabaseRule
import app.cash.turbine.test
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import nasa.apod.data.api.ApodApi
import nasa.apod.data.api.ApodJson
import nasa.core.model.ApiKey
import nasa.db.ApodDao
import nasa.db.NasaDatabase
import nasa.test.MockWebServerRule
import nasa.test.getResourceAsText
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

@RunWith(RobolectricTestRunner::class)
class MultipleApodRepositoryTest {
  @get:Rule
  val databaseRule = RoomDatabaseRule(NasaDatabase::class)

  @get:Rule
  val webServerRule = MockWebServerRule()

  @get:Rule
  val timberRule = TimberTestRule.logAllWhenTestFails()!!

  private lateinit var repository: MultipleApodRepository
  private lateinit var dao: ApodDao
  private lateinit var api: ApodApi

  @Before
  fun before() {
    dao = databaseRule.database.apodDao()
    api = webServerRule.buildApi(json = ApodJson)
  }

  @Test
  fun `Fetch this month and store in database`() = runTest {
    // Given a valid response is returned from the server
    buildRepo()
    webServerRule.enqueue(
      code = 200,
      body = readJsonFromResource(name = "multiple-full.json"),
    )

    dao.observeDates().test {
      // and the database doesn't have anything initially
      assertEquals(expected = 0, actual = awaitItem().size)

      // When we query for the latest from the API
      val result = repository.loadThisMonth(API_KEY)

      // Then the item is parsed and returned
      assertIs<MultipleLoadResult.Success>(result)
      assertEquals(expected = 30, actual = result.items.size)

      // and the API was queried once
      webServerRule.assertRequestCount(expected = 1)

      // and the database has 30 new entries
      assertEquals(expected = 30, actual = awaitItem().size)
    }
  }

  @Test
  fun `Requesting before valid range`() = runTest {
    // Given the server returns a successful
    buildRepo()
    webServerRule.enqueue(body = readJsonFromResource(name = "multiple-full.json"))

    // When we query the API for a date before the first available, but in the same month
    val invalidDate = LocalDate.parse("1995-06-01")
    repository.loadSpecificMonth(API_KEY, invalidDate)

    // then the start date request parameter is locked to the minimum possible value
    val requestUrl = webServerRule.server.takeRequest().requestUrl ?: fail("Null request URL")
    assertEquals(expected = "1995-06-16", actual = requestUrl.queryParameter(name = "start_date"))
    assertEquals(expected = "1995-06-30", actual = requestUrl.queryParameter(name = "end_date"))
  }

  @Test
  fun `Requesting after valid range`() = runTest {
    // When we query the API for a date after the current date (15th april), but in the same month
    buildRepo()
    val date = LocalDate.parse("2024-04-01")
    repository.loadSpecificMonth(API_KEY, date)

    // then the end date request parameter is locked before the current date
    val requestUrl = webServerRule.server.takeRequest().requestUrl ?: fail("Null request URL")
    assertEquals(expected = "2024-04-01", actual = requestUrl.queryParameter(name = "start_date"))
    assertEquals(expected = "2024-04-15", actual = requestUrl.queryParameter(name = "end_date"))
  }

  @Test
  fun `Fetch random month`() = runTest {
    // Given the server returns a successful random response
    buildRepo()
    webServerRule.enqueue(body = readJsonFromResource(name = "multiple-full.json"))

    dao.observeDates().test {
      // and the database doesn't have anything initially
      assertEquals(expected = 0, actual = awaitItem().size)

      // When we query for the latest from the API
      val result = repository.loadRandomMonth(API_KEY)

      // Then the item is parsed and returned
      assertIs<MultipleLoadResult.Success>(result)
      assertEquals(expected = 30, actual = result.items.size)

      // and the API was queried once
      webServerRule.assertRequestCount(expected = 1)

      // and the database has 30 new entries
      assertEquals(expected = 30, actual = awaitItem().size)
    }
  }

  private fun readJsonFromResource(name: String): String = getResourceAsText(name)

  private fun TestScope.buildRepo() {
    repository = MultipleApodRepository(
      io = IODispatcher(standardDispatcher),
      api = api,
      dao = dao,
      calendar = { TODAY },
      sharedRepository = SharedRepository(),
    )
  }

  private companion object {
    val API_KEY = ApiKey(value = "SOME_DUMMY_KEY")

    val TODAY = LocalDate.parse("2024-04-15")
  }
}
