package nasa.home.vm

import alakazam.android.core.UrlOpener
import alakazam.test.core.MainDispatcherRule
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import nasa.apod.data.repo.SingleApodRepository
import nasa.apod.data.repo.SingleLoadResult
import nasa.core.http.usage.ApiUsage
import nasa.core.http.usage.ApiUsageStateHolder
import nasa.core.model.ApiKey
import nasa.gallery.data.api.CollectionItem
import nasa.gallery.data.api.CollectionItemLink
import nasa.gallery.data.api.SearchItem
import nasa.gallery.data.repo.GallerySearchRepository
import nasa.gallery.data.repo.SearchResult
import nasa.gallery.model.Center
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.test.EXAMPLE_ITEM_1
import nasa.test.EXAMPLE_ITEM_2
import nasa.test.assertEmitted
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNull

class HomeViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  // real
  private lateinit var homeViewModel: HomeViewModel
  private lateinit var apiUsageStateHolder: ApiUsageStateHolder
  private lateinit var apiKeyState: MutableStateFlow<ApiKey?>
  private lateinit var apiKeyProvider: ApiKey.Provider

  // mock
  private lateinit var urlOpener: UrlOpener
  private lateinit var apodRepository: SingleApodRepository
  private lateinit var gallerySearchRepository: GallerySearchRepository

  @Before
  fun before() {
    apiUsageStateHolder = ApiUsageStateHolder()
    apiKeyState = MutableStateFlow(null)
    apiKeyProvider = ApiKey.Provider { apiKeyState }

    urlOpener = mockk(relaxed = true)
    apodRepository = mockk(relaxed = true)
    gallerySearchRepository = mockk(relaxed = true)

    buildViewModel()
  }

  @Test
  fun `Observe API usage`() = runTest {
    homeViewModel.apiUsage().test {
      // no key and no usage
      assertEmitted(ApiUsageState.NoApiKey)

      // demo key and no usage
      apiKeyState.update { ApiKey.DEMO }
      assertEmitted(ApiUsageState.DemoKeyNoUsage)

      // real key and no usage
      apiKeyState.update { TEST_KEY }
      assertEmitted(ApiUsageState.RealKeyNoUsage)

      // real key and some usage
      val usage = ApiUsage(remaining = 123, upperLimit = 234)
      apiUsageStateHolder.update { usage }
      assertEmitted(ApiUsageState.RealKeyHasUsage(remaining = 123, upperLimit = 234))

      // demo key and some usage
      apiKeyState.update { ApiKey.DEMO }
      assertEmitted(ApiUsageState.DemoKeyHasUsage(remaining = 123, upperLimit = 234))

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Observe APOD thumbnail URL`() = runTest {
    homeViewModel.apodThumbnailUrl.test {
      // null key -> null URL
      assertNull(apiKeyState.value)
      assertEmitted(null)

      // valid key
      setApodLoadResult(SingleLoadResult.Success(EXAMPLE_ITEM_1))
      apiKeyState.update { TEST_KEY }
      assertEmitted(EXAMPLE_ITEM_1.url) // no thumbnail, so get the full URL

      setApodLoadResult(SingleLoadResult.Success(EXAMPLE_ITEM_2))
      apiKeyState.update { ApiKey.DEMO }
      assertEmitted(EXAMPLE_ITEM_2.thumbnailUrl) // actually has a thumbnail

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Observe gallery thumbnail URL when response is failure`() = runTest {
    setGalleryLoadResult(SearchResult.Empty)
    buildViewModel()
    advanceUntilIdle()

    homeViewModel.galleryThumbnailUrl.test {
      // empty result -> null URL
      assertEmitted(null)

      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Observe gallery thumbnail URL when response is success`() = runTest {
    val thumbnailUrl = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB" +
      "-ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A~thumb.jpg"

    val items = listOf(
      CollectionItem(
        collectionUrl = JsonUrl(
          "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-" +
            "TO-PAD-39A/collection.json",
        ),
        data = listOf(
          SearchItem(
            center = Center("JSC"),
            title = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
            keywords = Keywords.from("Apollo", "Apollo 8", "NASA", "Film", "Film Transfers"),
            nasaId = NasaId("T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A"),
            dateCreated = Instant.parse("2017-11-16T00:00:00Z"),
            mediaType = MediaType.Video,
            description508 = "Apollo, Apollo 8, NASA, Film, Film Transfers",
            description = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
            location = null,
            photographer = null,
            secondaryCreator = null,
            album = null,
          ),
        ),
        links = listOf(
          CollectionItemLink(
            url = thumbnailUrl,
            rel = CollectionItemLink.Relation.Preview,
            render = "image",
          ),
          CollectionItemLink(
            url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
              "ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A.srt",
            rel = CollectionItemLink.Relation.Captions,
          ),
        ),
      ),
    )
    setGalleryLoadResult(
      SearchResult.Success(
        pagedResults = items,
        totalResults = 123,
        maxPerPage = 500,
        pageNumber = 1,
        prevPage = null,
        nextPage = 2,
      ),
    )
    buildViewModel()
    advanceUntilIdle()

    homeViewModel.galleryThumbnailUrl.test {
      assertEmitted(thumbnailUrl)
      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun setApodLoadResult(result: SingleLoadResult) {
    coEvery { apodRepository.loadToday(any()) } returns result
  }

  private fun setGalleryLoadResult(result: SearchResult) {
    coEvery { gallerySearchRepository.search(any(), any(), any()) } returns result
  }

  private fun buildViewModel() {
    homeViewModel = HomeViewModel(
      apiUsageStateHolder = apiUsageStateHolder,
      urlOpener = urlOpener,
      apiKeyProvider = apiKeyProvider,
      apodRepository = apodRepository,
      galleryRepository = gallerySearchRepository,
    )
  }

  private companion object {
    val TEST_KEY = ApiKey(value = "abc123")
  }
}
