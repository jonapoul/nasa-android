package nasa.apod.single.vm

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import nasa.core.http.DownloadProgressStateHolder
import nasa.core.http.DownloadState
import nasa.core.model.FileSize
import nasa.core.model.bytes
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
      setInProgress(read = 0.bytes)
      assertDownloadProgress(expected = 0f)

      setInProgress(read = 20.bytes)
      assertDownloadProgress(expected = 0.2f)

      setInProgress(read = 50.bytes)
      assertDownloadProgress(expected = 0.5f)

      setInProgress(read = 90.bytes)
      assertDownloadProgress(expected = 0.9f)

      setInProgress(read = 99.bytes)
      assertDownloadProgress(expected = 0.99f)

      setDone()
      assertDownloadProgress(expected = 1f)
      cancelAndIgnoreRemainingEvents()
    }
  }

  private suspend fun TurbineTestContext<Float>.assertDownloadProgress(expected: Float) {
    assertEquals(expected, awaitItem())
  }

  private fun setInProgress(read: FileSize) {
    stateHolder.set(
      DownloadState.InProgress(
        url = "some.url.com/whocares",
        read = read,
        total = DOWNLOAD_SIZE,
      ),
    )
  }

  private fun setDone() {
    stateHolder.set(
      DownloadState.Done(
        url = "some.url.com/whocares",
        total = DOWNLOAD_SIZE,
      ),
    )
  }

  private companion object {
    val DOWNLOAD_SIZE = 100.bytes
  }
}
