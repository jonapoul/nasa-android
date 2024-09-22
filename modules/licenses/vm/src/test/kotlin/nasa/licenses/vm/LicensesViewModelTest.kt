package nasa.licenses.vm

import alakazam.android.core.UrlOpener
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import nasa.licenses.data.LibraryModel
import nasa.licenses.data.LicenseModel
import nasa.licenses.data.LicensesLoadState
import nasa.licenses.data.LicensesRepository
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class LicensesViewModelTest {
  @get:Rule
  val timberRule = TimberTestRule.logAllWhenTestFails()!!

  // real
  private lateinit var viewModel: LicensesViewModel

  // mock
  private lateinit var repository: LicensesRepository
  private lateinit var urlOpener: UrlOpener

  @Before
  fun before() {
    repository = mockk(relaxed = true)
    urlOpener = mockk(relaxed = true)
  }

  @Test
  fun `Reload data after failure`() = runTest {
    // Given the repository data access fails
    val message = "something broke"
    coEvery { repository.loadLicenses() } returns LicensesLoadState.Failure(message)

    // When
    buildViewModel()

    viewModel.licensesState.test {
      // Then an error state is returned
      assertState(LicensesState.Error(message))

      // Given the repo now fetches successfully
      coEvery { repository.loadLicenses() } returns LicensesLoadState.Success(listOf(EXAMPLE_MODEL))

      // When
      viewModel.load()

      // Then a success state is returned
      assertState(LicensesState.Loading)
      assertLoaded(EXAMPLE_MODEL)
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

    viewModel.licensesState.test {
      // Then
      assertState(LicensesState.NoneFound)
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Open URL`() = runTest {
    // When
    val url = "www.website.com/whatever"
    buildViewModel()
    viewModel.openUrl(url)

    // Then
    verify(exactly = 1) { urlOpener.openUrl(url) }
    confirmVerified(urlOpener)
  }

  @Test
  fun `Toggle search bar and enter text`() = runTest {
    // Given the repo fetches a library successfully
    val models = listOf(EXAMPLE_MODEL)
    coEvery { repository.loadLicenses() } returns LicensesLoadState.Success(models)

    // When
    buildViewModel()

    // Then
    viewModel.searchBarState.test {
      assertEquals(expected = SearchBarState.Gone, actual = awaitItem())

      viewModel.toggleSearchBar()
      assertEquals(expected = SearchBarState.Visible(text = ""), actual = awaitItem())

      viewModel.setSearchText(text = "Hello world")
      assertEquals(expected = SearchBarState.Visible(text = "Hello world"), actual = awaitItem())

      viewModel.toggleSearchBar()
      assertEquals(expected = SearchBarState.Gone, actual = awaitItem())

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Filter licenses based on search results`() = runTest {
    // Given the repo fetches some libraries successfully
    val basicLib = EXAMPLE_MODEL
    val projectLib = EXAMPLE_MODEL.copy(project = "my project")
    val descriptionLib = EXAMPLE_MODEL.copy(description = "whatever")
    val versionLib = EXAMPLE_MODEL.copy(version = "7.8.9")
    val yearLib = EXAMPLE_MODEL.copy(year = 1993)
    val developersLib = EXAMPLE_MODEL.copy(developers = listOf("Tony Blair"))
    val urlLib = EXAMPLE_MODEL.copy(url = "www.url.com")
    val licenseLib = EXAMPLE_MODEL.copy(licenses = listOf(LicenseModel.MIT))
    val allLibraries = listOf(
      basicLib,
      projectLib,
      descriptionLib,
      versionLib,
      yearLib,
      developersLib,
      urlLib,
      licenseLib,
    )
    coEvery { repository.loadLicenses() } returns LicensesLoadState.Success(allLibraries)

    buildViewModel()

    viewModel.licensesState.test {
      // No filter
      assertLoaded(allLibraries)

      // Apply filters
      viewModel.toggleSearchBar()
      viewModel.setSearchText(text = "my project")
      assertLoaded(projectLib)

      viewModel.setSearchText(text = "99")
      assertLoaded(yearLib)

      viewModel.setSearchText(text = "url")
      assertLoaded(urlLib)

      viewModel.setSearchText(text = "MIT")
      assertLoaded(licenseLib)

      viewModel.setSearchText(text = "")
      assertLoaded(allLibraries)

      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun buildViewModel() {
    viewModel = LicensesViewModel(
      licensesRepository = repository,
      urlOpener = urlOpener,
    )
  }

  private suspend fun TurbineTestContext<LicensesState>.assertState(state: LicensesState) {
    assertEquals(expected = state, actual = awaitItem())
  }

  private suspend fun TurbineTestContext<LicensesState>.assertLoaded(vararg models: LibraryModel) {
    assertLoaded(models.toList())
  }

  private suspend fun TurbineTestContext<LicensesState>.assertLoaded(models: List<LibraryModel>) {
    assertState(LicensesState.Loaded(models.toImmutableList()))
  }

  private companion object {
    val EXAMPLE_MODEL = LibraryModel(
      project = "Something",
      description = "Whatever",
      version = "1.2.3",
      developers = listOf("Tom", "Dick", "Harry"),
      url = "www.website.com",
      year = 2024,
      licenses = listOf(LicenseModel.Apache2),
      dependency = "com.website:something",
    )
  }
}
