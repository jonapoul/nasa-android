package nasa.apod.vm.full

import app.cash.turbine.test
import io.mockk.mockk
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import nasa.core.http.progress.DownloadProgressStateHolder
import nasa.core.http.progress.DownloadState
import nasa.core.model.FileSize
import nasa.core.model.bytes
import nasa.core.model.percent
import nasa.test.assertEmitted
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ApodFullScreenViewModelTest {
  private lateinit var viewModel: ApodFullScreenViewModel
  private lateinit var stateHolder: DownloadProgressStateHolder

  @Before
  fun before() {
    stateHolder = DownloadProgressStateHolder()
    viewModel = ApodFullScreenViewModel(
      progressStateHolder = stateHolder,
      repository = mockk(),
    )
  }

  @Test
  fun `Monitor download progress`() = runTest {
    viewModel.downloadProgress.test {
      setInProgress(read = 0.bytes)
      assertEmitted(expected = 0.percent)

      setInProgress(read = 20.bytes)
      assertEmitted(expected = 20.percent)

      setInProgress(read = 50.bytes)
      assertEmitted(expected = 50.percent)

      setInProgress(read = 90.bytes)
      assertEmitted(expected = 90.percent)

      setInProgress(read = 99.bytes)
      assertEmitted(expected = 99.percent)

      setDone()
      assertEmitted(expected = 100.percent)
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun setInProgress(read: FileSize) {
    stateHolder.update {
      DownloadState.InProgress(
        url = "some.url.com/whocares",
        read = read,
        total = DOWNLOAD_SIZE,
      )
    }
  }

  private fun setDone() {
    stateHolder.update {
      DownloadState.Done(
        url = "some.url.com/whocares",
        total = DOWNLOAD_SIZE,
      )
    }
  }

  private companion object {
    val DOWNLOAD_SIZE = 100.bytes
  }
}
