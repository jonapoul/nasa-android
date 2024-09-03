package nasa.apod.vm.single

import alakazam.android.core.UrlOpener
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import nasa.apod.data.repo.FailureResult
import nasa.apod.data.repo.SingleApodRepository
import nasa.apod.data.repo.SingleLoadResult
import nasa.apod.model.ApodNavButtonsState
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.nav.ApodScreenConfig
import nasa.core.model.ApiKey
import nasa.core.model.Calendar
import nasa.test.EXAMPLE_ITEM_1
import nasa.test.InMemoryApiKeyProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class ApodSingleViewModelTest {
  // real
  private lateinit var viewModel: ApodSingleViewModel
  private lateinit var apiKeyProvider: InMemoryApiKeyProvider
  private lateinit var calendar: Calendar
  private lateinit var savedStateHandle: SavedStateHandle

  // mock
  private lateinit var repository: SingleApodRepository
  private lateinit var urlOpener: UrlOpener

  @Before
  fun before() {
    apiKeyProvider = InMemoryApiKeyProvider(API_KEY)
    calendar = Calendar { TODAY }
    savedStateHandle = SavedStateHandle()
    repository = mockk()
    urlOpener = mockk(relaxed = true)
    buildViewModel()
  }

  @Test
  fun `Loading random day`() = runTest {
    viewModel.state.test {
      // Given the repo is set to return an item successfully
      coEvery { repository.loadRandom(API_KEY) } returns SingleLoadResult.Success(EXAMPLE_ITEM_1)
      assertEquals(ScreenState.Inactive, awaitItem())

      // When we load with random config
      viewModel.load(API_KEY, ApodScreenConfig.Random())
      assertEquals(ScreenState.Loading(date = null, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // Then we get a success state
      assertEquals(
        expected = ScreenState.Success(EXAMPLE_ITEM_1, API_KEY),
        actual = awaitItem(),
      )

      // and the saved state is updated with the loaded date
      assertEquals(
        expected = EXAMPLE_ITEM_1.date.toString(),
        actual = savedStateHandle.get<String>("mostRecentDate"),
      )

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Loading today`() = runTest {
    viewModel.state.test {
      // Given the repo is set to return the data successfully
      coEvery { repository.loadToday(API_KEY) } returns SingleLoadResult.Success(EXAMPLE_ITEM_1)
      assertEquals(ScreenState.Inactive, awaitItem())

      // When we load with today config
      viewModel.load(API_KEY, ApodScreenConfig.Today)
      assertEquals(ScreenState.Loading(date = null, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // Then we get a success state
      assertEquals(
        expected = ScreenState.Success(EXAMPLE_ITEM_1, API_KEY),
        actual = awaitItem(),
      )

      // and the saved state is updated with the loaded date
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
      // Given the repo is set to return the data successfully
      val errorMessage = "foobar"
      coEvery { repository.loadToday(API_KEY) } returns FailureResult.OutOfRange(errorMessage)
      assertEquals(ScreenState.Inactive, awaitItem())

      // When we load the data
      viewModel.load(API_KEY, ApodScreenConfig.Today)
      assertEquals(ScreenState.Loading(date = null, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // Then we get a failure state
      val item = awaitItem()
      assertIs<ScreenState.Failed>(item)
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
    // Given the repo is set to return the data successfully
    val twelfthOfApril = LocalDate(year = 2024, month = Month.APRIL, dayOfMonth = 12)
    coEvery { repository.loadSpecific(API_KEY, twelfthOfApril) } returns SingleLoadResult.Success(EXAMPLE_ITEM_1)

    // and the saved state has a previously-loaded date saved. This happens if we selected random, so if we go back
    // to this screen from the nav stack, it'll reload the same random item
    savedStateHandle["mostRecentDate"] = twelfthOfApril.toString()
    buildViewModel()

    viewModel.state.test {
      assertEquals(ScreenState.Inactive, awaitItem())

      // When we load with a random config
      viewModel.load(API_KEY, ApodScreenConfig.Random())

      // then we start loading the previously-loaded date, not a new random one
      assertEquals(ScreenState.Loading(twelfthOfApril, API_KEY), awaitItem())
      testScheduler.advanceUntilIdle()

      // and we get a success state
      assertEquals(
        expected = ScreenState.Success(EXAMPLE_ITEM_1, API_KEY),
        actual = awaitItem(),
      )

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
      assertEquals(ScreenState.NoApiKey(null), awaitItem())
      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Loading the current date disables the next button`() = runTest {
    // Given the current date is in May
    val firstOfMay = LocalDate(year = 2024, month = Month.MAY, dayOfMonth = 1)
    calendar = Calendar { firstOfMay }
    buildViewModel()

    // and we can load data from May
    val item = EXAMPLE_ITEM_1.copy(date = firstOfMay)
    coEvery { repository.loadSpecific(API_KEY, firstOfMay) } returns SingleLoadResult.Success(item)

    viewModel.navButtonsState.test {
      // no data loaded yet, so both disabled
      assertEquals(ApodNavButtonsState.BothDisabled, awaitItem())

      // When we load data
      viewModel.load(API_KEY, ApodScreenConfig.Specific(firstOfMay))
      testScheduler.advanceUntilIdle()

      // Then the next button is disabled, since there isn't an available date after this one
      assertEquals(
        actual = awaitItem(),
        expected = ApodNavButtonsState(enablePrevious = true, enableNext = false),
      )
    }
  }

  @Test
  fun `Loading the earliest day disables the previous button`() = runTest {
    // Given we can load data from the earliest day
    val item = EXAMPLE_ITEM_1.copy(date = EARLIEST_APOD_DATE)
    coEvery { repository.loadSpecific(API_KEY, EARLIEST_APOD_DATE) } returns
      SingleLoadResult.Success(item)

    viewModel.navButtonsState.test {
      // no data loaded yet, so both disabled
      assertEquals(ApodNavButtonsState.BothDisabled, awaitItem())

      // When we load data
      viewModel.load(API_KEY, ApodScreenConfig.Specific(EARLIEST_APOD_DATE))
      testScheduler.advanceUntilIdle()

      // Then the previous button is disabled, since there isn't an available date before this one
      assertEquals(
        actual = awaitItem(),
        expected = ApodNavButtonsState(enablePrevious = false, enableNext = true),
      )
    }
  }

  private fun buildViewModel() {
    viewModel = ApodSingleViewModel(
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
  }
}
