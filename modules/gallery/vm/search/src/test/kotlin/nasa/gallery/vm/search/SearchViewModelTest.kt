package nasa.gallery.vm.search

import alakazam.kotlin.core.IODispatcher
import alakazam.kotlin.core.MainDispatcher
import alakazam.test.core.standardDispatcher
import alakazam.test.core.unconfinedDispatcher
import alakazam.test.db.RoomDatabaseRule
import app.cash.turbine.test
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import nasa.db.GalleryDao
import nasa.db.NasaDatabase
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.data.api.GalleryJson
import nasa.gallery.data.repo.GallerySearchRepository
import nasa.gallery.data.repo.SearchPreferences
import nasa.gallery.model.Center
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.NasaId
import nasa.gallery.model.SearchViewConfig
import nasa.gallery.model.SearchViewType.Card
import nasa.gallery.model.SearchViewType.Grid
import nasa.gallery.model.SubtitleUrl
import nasa.gallery.model.filterConfig
import nasa.gallery.model.year
import nasa.test.MockWebServerRule
import nasa.test.assertEmitted
import nasa.test.buildPreferences
import nasa.test.getResourceAsText
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_OK
import kotlin.time.Duration.Companion.milliseconds

@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {
  @get:Rule
  val databaseRule = RoomDatabaseRule(NasaDatabase::class)

  @get:Rule
  val webServerRule = MockWebServerRule()

  @get:Rule
  val timberRule = TimberTestRule.logAllWhenTestFails()!!

  // real
  private lateinit var viewModel: SearchViewModel
  private lateinit var gallerySearchRepository: GallerySearchRepository
  private lateinit var searchPreferences: SearchPreferences

  // mock
  private lateinit var galleryApi: GalleryApi
  private lateinit var galleryDao: GalleryDao

  private fun TestScope.before() {
    val standardDispatcher = standardDispatcher
    val preferences = buildPreferences(standardDispatcher)
    searchPreferences = SearchPreferences(preferences)

    galleryDao = databaseRule.database.galleryDao()
    galleryApi = webServerRule.buildApi(json = GalleryJson)

    gallerySearchRepository = GallerySearchRepository(
      io = IODispatcher(standardDispatcher),
      searchPreferences = searchPreferences,
      galleryApi = galleryApi,
      galleryDao = galleryDao,
    )

    viewModel = SearchViewModel(
      main = MainDispatcher(standardDispatcher),
      repository = gallerySearchRepository,
      searchPreferences = searchPreferences,
    )
  }

  @Test
  fun `Updating filter config`() = runTest {
    before()

    // debouncing because resetExtraConfig triggers three separate emissions - we just want the last one
    viewModel
      .filterConfig
      .debounce(100.milliseconds)
      .flowOn(unconfinedDispatcher)
      .test {
        assertEmitted(filterConfig())

        // input query
        viewModel.enterSearchTerm(text = "QUERY")
        assertEmitted(filterConfig(query = "QUERY"))

        // set start year
        viewModel.setFilterConfig(filterConfig(yearStart = 1999.year))
        assertEmitted(filterConfig(query = "QUERY", 1999.year))

        // set end year
        viewModel.setFilterConfig(filterConfig(yearStart = 1999.year, yearEnd = 2001.year))
        assertEmitted(filterConfig(query = "QUERY", 1999.year, 2001.year))

        // set media types
        viewModel.setFilterConfig(filterConfig(yearStart = 1999.year, yearEnd = 2001.year, mediaTypes = MediaTypes.All))
        assertEmitted(filterConfig(query = "QUERY", 1999.year, 2001.year, MediaTypes.All))

        // reset
        viewModel.resetExtraConfig()
        assertEmitted(filterConfig(query = "QUERY"))

        advanceUntilIdle()
        ensureAllEventsConsumed()
        cancelAndIgnoreRemainingEvents()
      }
  }

  @Test
  fun `Updating view config`() = runTest {
    before()

    // debouncing because sometimes we'll get a couple of emissions quickly - we just want the last one
    viewModel
      .viewConfig
      .debounce(100.milliseconds)
      .flowOn(unconfinedDispatcher)
      .test {
        val defaultCard = SearchViewConfig(Card, SearchPreferences.DEFAULT_GRID_COLUMN_WIDTH)
        assertEmitted(defaultCard)

        // Change view type
        val defaultGrid = SearchViewConfig(Grid, SearchPreferences.DEFAULT_GRID_COLUMN_WIDTH)
        viewModel.setViewConfig(defaultGrid)
        assertEmitted(defaultGrid)

        // Change view type
        val customGrid = SearchViewConfig(Grid, columnWidthDp = 80)
        viewModel.setViewConfig(customGrid)
        assertEmitted(customGrid)

        // reset
        viewModel.resetViewConfig()
        assertEmitted(defaultCard)

        advanceUntilIdle()
        ensureAllEventsConsumed()
        cancelAndIgnoreRemainingEvents()
      }
  }

  @Test
  fun `Perform search does nothing if no query was entered`() = runTest {
    before()

    viewModel.searchState.test {
      // Given
      assertEmitted(SearchState.NoAction)

      // When
      viewModel.enterSearchTerm(text = "")
      advanceUntilIdle()
      ensureAllEventsConsumed()

      viewModel.performSearch()
      advanceUntilIdle()

      // Then no state change
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Perform search with no results`() = runTest {
    before()
    viewModel.searchState.test {
      // Given
      val json = getResourceAsText(filename = "search-empty.json")
      webServerRule.enqueue(json, code = HTTP_OK)
      assertEmitted(SearchState.NoAction)

      // When
      viewModel.enterSearchTerm(text = "dickbutt")
      advanceUntilIdle()
      viewModel.performSearch()
      assertEmitted(SearchState.Searching)

      // Then no results
      assertEmitted(SearchState.Empty)
      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Perform search with failure response`() = runTest {
    before()
    viewModel.searchState.test {
      // Given
      val json = getResourceAsText(filename = "search-failure.json")
      webServerRule.enqueue(json, code = HTTP_BAD_REQUEST)
      assertEmitted(SearchState.NoAction)

      // When
      viewModel.enterSearchTerm(text = "abc")
      advanceUntilIdle()
      viewModel.performSearch()
      assertEmitted(SearchState.Searching)

      // Then failure state
      val reason = "Expected 'q' text search parameter or other keywords."
      assertEmitted(SearchState.Failed(reason))
      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Perform search with success response`() = runTest {
    before()
    viewModel.searchState.test {
      // Given
      val json = getResourceAsText(filename = "search-success.json")
      webServerRule.enqueue(json, code = HTTP_OK)
      assertEmitted(SearchState.NoAction)
      val pageSize = 50
      searchPreferences.pageSize.setAndCommit(pageSize)

      // When
      viewModel.enterSearchTerm(text = "abc")
      advanceUntilIdle()
      viewModel.performSearch()
      assertEmitted(SearchState.Searching)

      // Then failure state
      val expected = SearchState.Success(
        results = persistentListOf(
          SearchResultItem(
            nasaId = NasaId("T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A"),
            collectionUrl = JsonUrl(
              url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
                "ENROUTE-TO-PAD-39A/collection.json",
            ),
            previewUrl = ImageUrl(
              url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
                "ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A~thumb.jpg",
            ),
            captionsUrl = SubtitleUrl(
              url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
                "ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A.srt",
            ),
            albums = null,
            center = Center("JSC"),
            title = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
            keywords = Keywords("Apollo", "Apollo 8", "NASA", "Film", "Film Transfers"),
            location = null,
            photographer = null,
            dateCreated = Instant.parse("2017-11-16T00:00:00Z"),
            mediaType = MediaType.Video,
            secondaryCreator = null,
            description = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
            description508 = "Apollo, Apollo 8, NASA, Film, Film Transfers",
          ),
        ),
        totalResults = 6324,
        resultsPerPage = pageSize,
        prevPageNumber = null,
        pageNumber = 1,
        nextPageNumber = 2,
      )
      assertEmitted(expected)
      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }
}
