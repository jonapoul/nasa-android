package apod.single.vm

import apod.core.http.DownloadProgressStateHolder
import apod.core.http.DownloadState
import app.cash.turbine.Turbine
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class ApodFullScreenViewModelTest {
  private lateinit var viewModel: ApodFullScreenViewModel
  private lateinit var stateHolder: DownloadProgressStateHolder

  @Before
  fun before() {
    stateHolder = DownloadProgressStateHolder()
    viewModel = ApodFullScreenViewModel(
      stateHolder = stateHolder,
    )
  }

  @Test
  fun `Monitor download progress`() = runTest {
    viewModel.downloadProgress.test {
      setDownloadState(read = 0L, total = 100L)
      assertDownloadProgress(0f)

      setDownloadState(read = 20L, total = 100L)
      assertDownloadProgress(0.2f)

      setDownloadState(read = 50L, total = 100L)
      assertDownloadProgress(0.5f)

      setDownloadState(read = 90L, total = 100L)
      assertDownloadProgress(0.9f)

      setDownloadState(read = 99L, total = 100L)
      assertDownloadProgress(0.99f)

      setDownloadState(read = 100L, total = 100L, done = true)
      assertDownloadProgress(1f)
      cancelAndIgnoreRemainingEvents()
    }
  }

  private suspend fun TurbineTestContext<Float>.assertDownloadProgress(expected: Float)  {
    assertEquals(expected, awaitItem())
  }

  private fun setDownloadState(read: Long, total: Long, done: Boolean = false) {
    stateHolder.set(
      DownloadState(
        url = "some.url.com/whocares",
        bytesRead = read,
        contentLength = total,
        done = done,
      )
    )
  }
}
