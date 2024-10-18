package nasa.apod.vm.grid

import alakazam.android.core.UrlOpener
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import nasa.apod.data.repo.FailureResult
import nasa.apod.data.repo.MultipleApodRepository
import nasa.apod.data.repo.MultipleLoadResult
import nasa.apod.model.ApodNavButtonsState
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.ui.grid.ApodGridViewModel
import nasa.apod.ui.grid.GridScreenState
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import nasa.test.EXAMPLE_ITEM_1
import nasa.test.EXAMPLE_ITEM_2
import nasa.test.InMemoryApiKeyProvider
import nasa.test.assertEmitted
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
  private lateinit var apiKeyProvider: InMemoryApiKeyProvider
  private lateinit var savedStateHandle: SavedStateHandle
  private lateinit var calendar: Calendar

  // mock
  private lateinit var repository: MultipleApodRepository
  private lateinit var urlOpener: UrlOpener

  @Before
  fun before() {
    savedStateHandle = SavedStateHandle(initialState = emptyMap())
    apiKeyProvider = InMemoryApiKeyProvider(API_KEY)
    calendar = Calendar { TODAY }
    repository = mockk()
    urlOpener = mockk(relaxed = true)
    buildViewModel()
  }

  @Test
  fun `Loading random month`() = runTest {
    viewModel.state.test {
      // Given the repo is set to return the month successfully
      coEvery { repository.loadRandomMonth(API_KEY) } returns MultipleLoadResult.Success(EXAMPLE_ITEMS)
      assertEmitted(GridScreenState.Inactive)

      // When we load with random config
      viewModel.load(API_KEY, ApodScreenConfig.Random())
      assertEmitted(GridScreenState.Loading(date = null, API_KEY))
      testScheduler.advanceUntilIdle()

      // Then we get a success state
      assertEmitted(GridScreenState.Success(EXAMPLE_ITEMS, API_KEY))

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
      assertEmitted(GridScreenState.Inactive)

      // When we load with today config
      viewModel.load(API_KEY, ApodScreenConfig.Today)
      assertEmitted(GridScreenState.Loading(date = null, API_KEY))
      testScheduler.advanceUntilIdle()

      // Then we get a success state
      assertEmitted(GridScreenState.Success(EXAMPLE_ITEMS, API_KEY))

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
      assertEmitted(GridScreenState.Inactive)

      // When we load the data
      viewModel.load(API_KEY, ApodScreenConfig.Today)
      assertEmitted(GridScreenState.Loading(date = null, API_KEY))
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
    val april = LocalDate(year = 2024, month = Month.APRIL, dayOfMonth = 1)
    coEvery { repository.loadSpecificMonth(API_KEY, april) } returns MultipleLoadResult.Success(EXAMPLE_ITEMS)

    // and the saved state has a previously-loaded date saved. This happens if we selected random, so if we go back
    // to this screen from the nav stack, it'll reload the same random item
    savedStateHandle["mostRecentDate"] = april.toString()
    buildViewModel()

    viewModel.state.test {
      assertEmitted(GridScreenState.Inactive)

      // When we load with a random config
      viewModel.load(API_KEY, ApodScreenConfig.Random())

      // then we start loading the previously-loaded month, not a new random one
      assertEmitted(GridScreenState.Loading(april, API_KEY))
      testScheduler.advanceUntilIdle()

      // and we get a success state
      assertEmitted(GridScreenState.Success(persistentListOf(EXAMPLE_ITEM_1, EXAMPLE_ITEM_2), API_KEY))

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Handle null API key`() = runTest {
    // Given we have no API key saved
    apiKeyProvider.set(null)
    buildViewModel()
    testScheduler.advanceUntilIdle()

    viewModel.state.test {
      // Then the UI is updated to tell the user
      assertEmitted(GridScreenState.NoApiKey)
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Loading the current month disables the next button`() = runTest {
    // Given the current date is in May
    val may = LocalDate(year = 2024, month = Month.MAY, dayOfMonth = 1)
    calendar = Calendar { may }
    buildViewModel()

    // and we can load data from May
    val item = EXAMPLE_ITEM_1.copy(date = may)
    coEvery { repository.loadSpecificMonth(API_KEY, may) } returns MultipleLoadResult.Success(persistentListOf(item))

    viewModel.navButtonsState.test {
      // no data loaded yet, so both disabled
      assertEmitted(ApodNavButtonsState.BothDisabled)

      // When we load data
      viewModel.load(API_KEY, ApodScreenConfig.Specific(may))
      testScheduler.advanceUntilIdle()

      // Then the next button is disabled, since there isn't an available month after this one
      assertEmitted(ApodNavButtonsState(enablePrevious = true, enableNext = false))
    }
  }

  @Test
  fun `Loading the earliest month disables the previous button`() = runTest {
    // Given we can load data from the earliest month
    val item = EXAMPLE_ITEM_1.copy(date = EARLIEST_APOD_DATE)
    coEvery { repository.loadSpecificMonth(API_KEY, EARLIEST_APOD_DATE) } returns
      MultipleLoadResult.Success(persistentListOf(item))

    viewModel.navButtonsState.test {
      // no data loaded yet, so both disabled
      assertEmitted(ApodNavButtonsState.BothDisabled)

      // When we load data
      viewModel.load(API_KEY, ApodScreenConfig.Specific(EARLIEST_APOD_DATE))
      testScheduler.advanceUntilIdle()

      // Then the previous button is disabled, since there isn't an available month before this one
      assertEmitted(ApodNavButtonsState(enablePrevious = false, enableNext = true))
    }
  }

  private fun buildViewModel() {
    viewModel = ApodGridViewModel(
      repository = repository,
      urlOpener = urlOpener,
      apiKeyProvider = apiKeyProvider,
      calendar = calendar,
      savedState = savedStateHandle,
    )
  }

  private companion object {
    val API_KEY = ApiKey(value = "my-api-key")
    val TODAY = LocalDate.parse("2024-05-03")

    val EXAMPLE_ITEMS = persistentListOf(EXAMPLE_ITEM_1, EXAMPLE_ITEM_2)
  }
}
