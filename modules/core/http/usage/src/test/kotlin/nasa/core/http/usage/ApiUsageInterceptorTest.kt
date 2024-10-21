package nasa.core.http.usage

import app.cash.turbine.test
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nasa.core.http.factories.buildOkHttp
import nasa.test.MockWebServerRule
import nasa.test.assertEmitted
import okhttp3.Headers
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.http.GET
import java.net.HttpURLConnection.HTTP_OK

class ApiUsageInterceptorTest {
  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var apiUsageInterceptor: ApiUsageInterceptor
  private lateinit var stateHolder: ApiUsageStateHolder
  private lateinit var testApi: TestApi

  @Before
  fun before() {
    stateHolder = ApiUsageStateHolder()
    apiUsageInterceptor = ApiUsageInterceptor(stateHolder)

    val client = buildOkHttp(debug = true, apiUsageInterceptor, log = { println(it) })
    testApi = webServerRule.buildApi(client = client)
  }

  @Test
  fun `Do nothing if no relevant headers are included in response`() = runTest {
    // Given response is queued up with irrelevant headers
    webServerRule.enqueueWithHeaders(
      "Something",
      "Whatever",
      "Set-Cookie",
      "ABC123",
    )

    stateHolder.test {
      // and no existing usage has been stored
      assertEmitted(null)

      // When the response is fetched
      println("Getting")
      testApi.getTestData()
      println("advancing")
      advanceUntilIdle()
      println("advanced")

      // Then no usage state is emitted
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Save state if usage headers are included in response`() = runTest {
    stateHolder.test {
      // Given no existing usage has been stored
      assertEmitted(null)

      // and a response is queued up with usage headers
      webServerRule.enqueueWithHeaders(
        "X-Ratelimit-Remaining",
        "123",
        "X-Ratelimit-Limit",
        "42069",
      )

      // When the response is fetched
      testApi.getTestData()

      // Then usage state is emitted
      assertEmitted(ApiUsage(remaining = 123, upperLimit = 42069))

      // When another response is queued and fetched
      webServerRule.enqueueWithHeaders(
        "X-Ratelimit-Remaining",
        "122",
        "X-Ratelimit-Limit",
        "42069",
      )
      testApi.getTestData()

      // Then updated usage state is emitted
      assertEmitted(ApiUsage(remaining = 122, upperLimit = 42069))
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun MockWebServerRule.enqueueWithHeaders(vararg headers: String) {
    val actualHeaders = headers.toList() + listOf("Content-Length", TEST_JSON.length.toString())
    enqueue {
      setResponseCode(HTTP_OK)
      setBody(TEST_JSON)
      setHeaders(Headers.headersOf(*actualHeaders.toTypedArray()))
    }
  }

  private interface TestApi {
    @GET("/dummy")
    suspend fun getTestData(): Response<TestData>
  }

  @Serializable
  private data class TestData(
    @SerialName("abc")
    val abc: Int,
  )

  private companion object {
    @Language("JSON")
    const val TEST_JSON = """{ "abc" : 123 }"""
  }
}
