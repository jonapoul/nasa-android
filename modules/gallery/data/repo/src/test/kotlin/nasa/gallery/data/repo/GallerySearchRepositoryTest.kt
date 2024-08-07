package nasa.gallery.data.repo

import alakazam.kotlin.core.IODispatcher
import alakazam.test.core.CoroutineRule
import alakazam.test.db.RoomDatabaseRule
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import nasa.db.RoomAlbumDao
import nasa.db.RoomAlbumDaoWrapper
import nasa.db.RoomCenterDao
import nasa.db.RoomCenterDaoWrapper
import nasa.db.RoomKeywordDao
import nasa.db.RoomKeywordDaoWrapper
import nasa.db.RoomNasaDatabase
import nasa.db.RoomPhotographerDao
import nasa.db.RoomPhotographerDaoWrapper
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.data.api.GalleryJson
import nasa.gallery.data.api.SearchItem
import nasa.gallery.data.api.SearchItemData
import nasa.gallery.data.api.SearchItemLink
import nasa.gallery.model.Center
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.test.MockWebServerRule
import nasa.test.buildPreferences
import nasa.test.getResourceAsText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_OK
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class GallerySearchRepositoryTest {
  @get:Rule
  val coroutineRule = CoroutineRule()

  @get:Rule
  val databaseRule = RoomDatabaseRule(RoomNasaDatabase::class)

  @get:Rule
  val webServerRule = MockWebServerRule()

  // real
  private lateinit var repository: GallerySearchRepository
  private lateinit var searchPreferences: SearchPreferences
  private lateinit var centerDao: RoomCenterDao
  private lateinit var keywordDao: RoomKeywordDao
  private lateinit var photographerDao: RoomPhotographerDao
  private lateinit var albumDao: RoomAlbumDao

  // fakes
  private lateinit var galleryApi: GalleryApi

  @Before
  fun before() {
    galleryApi = webServerRule.buildApi(json = GalleryJson)

    searchPreferences = SearchPreferences(buildPreferences(coroutineRule.dispatcher))
    val db = databaseRule.database
    centerDao = db.centreDao()
    keywordDao = db.keywordDao()
    photographerDao = db.photographerDao()
    albumDao = db.albumDao()

    repository = GallerySearchRepository(
      io = IODispatcher(coroutineRule.dispatcher),
      galleryApi = galleryApi,
      searchPreferences = searchPreferences,
      centerDao = RoomCenterDaoWrapper(centerDao),
      keywordDao = RoomKeywordDaoWrapper(keywordDao),
      photographerDao = RoomPhotographerDaoWrapper(photographerDao),
      albumDao = RoomAlbumDaoWrapper(albumDao),
    )
  }

  @Test
  fun `Handle empty filter`() = runTest {
    // when
    val result = repository.search(FilterConfig.Empty, pageNumber = null)

    // then
    assertEquals(expected = SearchResult.NoFilterSupplied, actual = result)
  }

  @Test
  fun `Handle failure`() = runTest {
    // given
    val json = this@GallerySearchRepositoryTest.getResourceAsText("search-failure.json")
    webServerRule.enqueue(json, code = HTTP_NOT_FOUND)

    // when
    val config = FilterConfig(query = "whatever")
    val result = repository.search(config, pageNumber = null)

    // then
    assertEquals(
      actual = result,
      expected = SearchResult.Failure(
        config = config,
        reason = "Expected 'q' text search parameter or other keywords.",
      ),
    )
  }

  @Test
  fun `Handle success response`() = runTest {
    // given
    val json = this@GallerySearchRepositoryTest.getResourceAsText("search-success.json")
    webServerRule.enqueue(json, code = HTTP_OK)

    val pageSize = 50
    searchPreferences.pageSize.setAndCommit(pageSize)

    // when
    val filter = FilterConfig(query = "foobar")
    val result = repository.search(filter, pageNumber = null)

    // then
    assertEquals(
      actual = result,
      expected = SearchResult.Success(
        pagedResults = listOf(
          SearchItem(
            collectionUrl = COLLECTION_URL,
            data = listOf(SEARCH_ITEM_DATA),
            links = listOf(
              SearchItemLink(url = IMAGE_URL, rel = SearchItemLink.Relation.Preview, render = "image"),
              SearchItemLink(url = CAPTIONS_URL, rel = SearchItemLink.Relation.Captions),
            ),
          ),
        ),
        totalResults = 6324,
        maxPerPage = pageSize,
        pageNumber = 1,
        prevPage = null,
        nextPage = 2,
      ),
    )

    assertEquals(
      expected = listOf("JSC"),
      actual = centerDao.getAll().map { it.center.value },
    )
    assertEquals(
      expected = listOf("Apollo", "Apollo 8", "Film", "Film Transfers", "NASA"),
      actual = keywordDao.getAll().map { it.keyword.value },
    )

    assertEquals(expected = emptyList(), actual = albumDao.getAll())
    assertEquals(expected = emptyList(), actual = photographerDao.getAll())
  }

  private companion object {
    val COLLECTION_URL = JsonUrl(
      "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-" +
        "TO-PAD-39A/collection.json",
    )

    val IMAGE_URL = ImageUrl(
      "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB" +
        "-ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A~thumb.jpg",
    )

    val CAPTIONS_URL = ImageUrl(
      "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
        "ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A.srt",
    )

    val SEARCH_ITEM_DATA = SearchItemData(
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
    )
  }
}
