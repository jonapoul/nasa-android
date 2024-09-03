package nasa.licenses.vm

import alakazam.android.core.UrlOpener
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import nasa.licenses.data.LibraryModel
import nasa.licenses.data.LicenseModel
import nasa.licenses.data.LicensesLoadState
import nasa.licenses.data.LicensesRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class LicensesViewModelTest {
  // real
  private lateinit var viewModel: LicensesViewModel

  // mock
  private lateinit var repository: LicensesRepository
  private lateinit var urlOpener: UrlOpener

  @Before
  fun before() {
    repository = mockk(relaxed = true)
    urlOpener = mockk(relaxed = true)
    buildViewModel()
  }

  @Test
  fun `Reload data after failure`() = runTest {
    // Given the repository data access fails
    val message = "something broke"
    coEvery { repository.loadLicenses() } returns LicensesLoadState.Failure(message)

    // When
    buildViewModel()

    viewModel.state.test {
      // Then an error state is returned
      assertEquals(LicensesState.Error(message), awaitItem())

      // Given the repo now fetches successfully
      val model = LibraryModel(
        project = "Something",
        description = "Whatever",
        version = "1.2.3",
        developers = listOf("Tom", "Dick", "Harry"),
        url = "www.website.com",
        year = 2024,
        licenses = listOf(LicenseModel.Apache2),
        dependency = "com.website:something",
      )
      coEvery { repository.loadLicenses() } returns LicensesLoadState.Success(listOf(model))

      // When
      viewModel.load()

      // Then a success state is returned
      assertEquals(LicensesState.Loading, awaitItem())
      assertEquals(LicensesState.Loaded(persistentListOf(model)), awaitItem())
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Handle empty licenses list`() = runTest {
    // Given the repo now fetches successfully, but nothing is in the list
    coEvery { repository.loadLicenses() } returns LicensesLoadState.Success(emptyList())

    // When
    buildViewModel()

    viewModel.state.test {
      // Then
      assertEquals(LicensesState.NoneFound, awaitItem())
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Open URL`() = runTest {
    // When
    val url = "www.website.com/whatever"
    viewModel.openUrl(url)

    // Then
    verify(exactly = 1) { urlOpener.openUrl(url) }
    confirmVerified(urlOpener)
  }

  private fun buildViewModel() {
    viewModel = LicensesViewModel(
      licensesRepository = repository,
      urlOpener = urlOpener,
    )
  }
}
