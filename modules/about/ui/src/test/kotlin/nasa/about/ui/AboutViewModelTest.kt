package nasa.about.ui

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import nasa.about.data.GithubReleaseModel
import nasa.about.data.GithubRepository
import nasa.about.data.LatestReleaseState
import nasa.core.android.UrlOpener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class AboutViewModelTest {
  // real
  private lateinit var viewModel: AboutViewModel

  // mock
  private lateinit var repository: GithubRepository
  private lateinit var urlOpener: UrlOpener

  @Before
  fun before() {
    repository = mockk()
    urlOpener = mockk(relaxed = true)
    viewModel = AboutViewModel(
      buildConfig = TestBuildConfig,
      githubRepository = repository,
      urlOpener = urlOpener,
    )
  }

  @Test
  fun `Successfully fetch new release`() = runTest {
    // Given the repo returns a new update
    val url = "www.website.com/whatever"
    val version = "2.3.4"
    val model = GithubReleaseModel(version, TestInstant, url)
    coEvery { repository.fetchLatestRelease() } returns LatestReleaseState.UpdateAvailable(model)

    viewModel.checkUpdatesState.test {
      assertEquals(CheckUpdatesState.Inactive, awaitItem())

      // When we fetch updates
      viewModel.fetchLatestRelease()
      assertEquals(CheckUpdatesState.Checking, awaitItem())

      // Then an update is returned
      assertEquals(CheckUpdatesState.UpdateFound(version, url), awaitItem())

      // When we cancel the dialog
      viewModel.cancelUpdateCheck()

      // Then the dialog is dismissed
      assertEquals(CheckUpdatesState.Inactive, awaitItem())
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Open issues page`() = runTest {
    // When
    viewModel.reportIssues()

    // Then
    verify(exactly = 1) { urlOpener.openUrl("https://github.com/jonapoul/nasa-android/issues/new") }
    confirmVerified(urlOpener)
  }

  @Test
  fun `Open repo page`() = runTest {
    // When
    viewModel.openRepo()

    // Then
    verify(exactly = 1) { urlOpener.openUrl("https://github.com/jonapoul/nasa-android") }
    confirmVerified(urlOpener)
  }
}
