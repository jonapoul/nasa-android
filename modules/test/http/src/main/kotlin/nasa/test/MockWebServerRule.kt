package nasa.test

import kotlinx.serialization.json.Json
import nasa.core.http.factories.buildOkHttp
import nasa.core.http.factories.buildRetrofit
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.create
import kotlin.test.assertEquals

class MockWebServerRule : TestWatcher() {
  lateinit var server: MockWebServer

  override fun starting(description: Description?) {
    super.starting(description)
    server = MockWebServer()
  }

  override fun finished(description: Description?) {
    super.finished(description)
    server.shutdown()
  }

  fun enqueue(body: String, code: Int = 200) {
    enqueue {
      setResponseCode(code)
      setBody(body)
    }
  }

  fun enqueue(builder: MockResponse.() -> MockResponse) {
    enqueue(MockResponse().builder())
  }

  fun enqueue(response: MockResponse) {
    server.enqueue(response)
  }

  fun assertRequestCount(expected: Int) {
    assertEquals(expected = expected, actual = server.requestCount)
  }

  inline fun <reified T : Any> buildApi(json: Json = Json): T {
    val retrofit = buildRetrofit(
      client = buildOkHttp(debug = true, log = null),
      url = server.url(path = "/").toString(),
      json = json,
    )
    return retrofit.create<T>()
  }
}
