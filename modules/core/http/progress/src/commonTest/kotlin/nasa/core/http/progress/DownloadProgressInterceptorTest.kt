package nasa.core.http.progress

import app.cash.turbine.test
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import nasa.core.http.factories.buildOkHttp
import nasa.core.http.progress.DownloadState.Done
import nasa.core.http.progress.DownloadState.InProgress
import nasa.core.model.bytes
import nasa.test.MockWebServerRule
import nasa.test.assertEmitted
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.net.HttpURLConnection.HTTP_OK
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DownloadProgressInterceptorTest {
  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var downloadProgressInterceptor: DownloadProgressInterceptor
  private lateinit var stateHolder: DownloadProgressStateHolder
  private lateinit var testApi: TestApi

  @Before
  fun before() {
    stateHolder = DownloadProgressStateHolder()
    downloadProgressInterceptor = DownloadProgressInterceptor(stateHolder)

    val client = buildOkHttp(debug = true, downloadProgressInterceptor, log = null)
    testApi = webServerRule.buildApi(client = client, ScalarsConverterFactory.create())
  }

  @Test
  fun `Do nothing if no relevant headers are included in request`() = runTest {
    // Given response is queued up with irrelevant headers
    webServerRule.enqueueData()

    stateHolder.test {
      // and no existing progress has been stored
      assertEmitted(null)

      // When the response is fetched
      testApi.getTestDataWithoutRequestHeader()
      advanceUntilIdle()

      // Then no progress state is emitted
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Save state if progress headers are included in request`() = runTest {
    stateHolder.test {
      // Given no existing progress has been stored
      assertEmitted(null)

      // and a response is queued up with progress flag header
      webServerRule.enqueueData()

      // When the response is fetched with header on the request
      testApi.getTestDataWithRequestHeader()

      // Then progress state is emitted
      var state = awaitItem()
      while (state !is Done) {
        assertIs<InProgress>(state)
        state = awaitItem()
      }

      // Then updated progress state is emitted
      assertIs<Done>(state)
      assertEquals(expected = DATA_SIZE.bytes, actual = state.total)
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun MockWebServerRule.enqueueData() {
    val data = ByteArray(DATA_SIZE) { (it % 26).toByte() }.decodeToString()

    enqueue {
      setResponseCode(HTTP_OK)
      setBody(data)
    }
  }

  private interface TestApi {
    @GET("/test")
    suspend fun getTestDataWithoutRequestHeader(): Response<String>

    @GET("/test")
    suspend fun getTestDataWithRequestHeader(
      @Header(DownloadProgressInterceptor.HEADER) header: String = "foobar",
    ): Response<String>
  }

  private companion object {
    const val DATA_SIZE = 1_000_000
  }
}
