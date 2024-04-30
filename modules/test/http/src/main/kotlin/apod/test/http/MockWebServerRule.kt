package apod.test.http

import apod.core.http.buildOkHttp
import apod.core.http.buildRetrofit
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.create

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
      setBody(body)
      setResponseCode(code)
    }
  }

  fun enqueue(builder: MockResponse.() -> MockResponse) {
    enqueue(MockResponse().apply { builder() })
  }

  fun enqueue(response: MockResponse) {
    server.enqueue(response)
  }

  inline fun <reified T : Any> buildApi(json: Json = Json): T {
    val retrofit = buildRetrofit(
      client = buildOkHttp(log = null),
      url = server.url(path = "/").toString(),
      json = json,
    )
    return retrofit.create<T>()
  }
}
