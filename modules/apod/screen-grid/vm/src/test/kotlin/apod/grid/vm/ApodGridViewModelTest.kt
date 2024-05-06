package apod.grid.vm

import androidx.lifecycle.SavedStateHandle
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import apod.core.url.UrlOpener
import apod.data.repo.FailureResult
import apod.data.repo.MultipleApodRepository
import apod.data.repo.MultipleLoadResult
import apod.nav.ScreenConfig
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class ApodGridViewModelTest {
  // real
  private lateinit var viewModel: ApodGridViewModel
  private lateinit var apiKeyProvider: ApiKeyProvider
  private lateinit var savedStateHandle: SavedStateHandle

  // mock
  private lateinit var repository: MultipleApodRepository
  private lateinit var urlOpener: UrlOpener

  @Before
  fun before() {
    savedStateHandle = SavedStateHandle(initialState = emptyMap())
    apiKeyProvider = ApiKeyProvider { flowOf(API_KEY) }
    repository = mockk()
    urlOpener = mockk(relaxed = true)
    buildViewModel()
  }

  @Test
  fun `Loading random month`() = runTest {
    viewModel.state.test {
      // Given the repo is set to return the month successfully
      coEvery { repository.loadRandomMonth(API_KEY) } returns MultipleLoadResult.Success(EXAMPLE_ITEMS)
      assertEquals(GridScreenState.Inactive, awaitItem())

      // When we load with random config
      viewModel.load(API_KEY, ScreenConfig.Random())
      assertEquals(GridScreenState.Loading(date = null, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // Then we get a success state
      assertEquals(
        expected = GridScreenState.Success(EXAMPLE_ITEMS, API_KEY),
        actual = awaitItem(),
      )

      // and the saved state is updated with the loaded month
      assertEquals(
        expected = EXAMPLE_ITEM_1.date.toString(),
        actual = savedStateHandle.get<String>("mostRecentDate"),
      )

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Loading this month`() = runTest {
    viewModel.state.test {
      // Given the repo is set to return the month successfully
      coEvery { repository.loadThisMonth(API_KEY) } returns MultipleLoadResult.Success(EXAMPLE_ITEMS)
      assertEquals(GridScreenState.Inactive, awaitItem())

      // When we load with today config
      viewModel.load(API_KEY, ScreenConfig.Today)
      assertEquals(GridScreenState.Loading(date = null, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // Then we get a success state
      assertEquals(
        expected = GridScreenState.Success(EXAMPLE_ITEMS, API_KEY),
        actual = awaitItem(),
      )

      // and the saved state is updated with the loaded month
      assertEquals(
        expected = EXAMPLE_ITEM_1.date.toString(),
        actual = savedStateHandle.get<String>("mostRecentDate"),
      )

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Handle load failure`() = runTest {
    viewModel.state.test {
      // Given the repo is set to return the month successfully
      val errorMessage = "foobar"
      coEvery { repository.loadThisMonth(API_KEY) } returns FailureResult.OutOfRange(errorMessage)
      assertEquals(GridScreenState.Inactive, awaitItem())

      // When we load the data
      viewModel.load(API_KEY, ScreenConfig.Today)
      assertEquals(GridScreenState.Loading(date = null, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // Then we get a failure state
      val item = awaitItem()
      assertIs<GridScreenState.Failed>(item)
      assertTrue(item.message.contains(errorMessage))

      // and the saved state is NOT updated with anything, because the request failed and we didn't supply a specific
      // date
      assertFalse(savedStateHandle.contains("mostRecentDate"))

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Reload previous date, even if in random config`() = runTest {
    // Given the repo is set to return the month successfully
    val april = LocalDate(year = 2024, month =  Month.APRIL, dayOfMonth = 1)
    coEvery { repository.loadSpecificMonth(API_KEY, april) } returns MultipleLoadResult.Success(EXAMPLE_ITEMS)

    // and the saved state has a previously-loaded date saved. This happens if we selected random, so if we go back
    // to this screen from the nav stack, it'll reload the same random item
    savedStateHandle["mostRecentDate"] = april.toString()
    buildViewModel()

    viewModel.state.test {
      assertEquals(GridScreenState.Inactive, awaitItem())

      // When we load with a random config
      viewModel.load(API_KEY, ScreenConfig.Random())

      // then we start loading the previously-loaded month, not a new random one
      assertEquals(GridScreenState.Loading(april, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // and we get a success state
      assertEquals(
        expected = GridScreenState.Success(persistentListOf(EXAMPLE_ITEM_1, EXAMPLE_ITEM_2), API_KEY),
        actual = awaitItem(),
      )

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Handle null API key`() = runTest {
    // Given we have no API key saved
    apiKeyProvider = ApiKeyProvider { flowOf(null) }
    buildViewModel()
    testScheduler.advanceUntilIdle()

    viewModel.state.test {
      // Then the UI is updated to tell the user
      assertEquals(GridScreenState.NoApiKey, awaitItem())
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun buildViewModel() {
    viewModel = ApodGridViewModel(
      repository = repository,
      urlOpener = urlOpener,
      apiKeyProvider = apiKeyProvider,
      savedState = savedStateHandle,
    )
  }

  private companion object {
    val API_KEY = ApiKey(value = "my-api-key")
  }
}
