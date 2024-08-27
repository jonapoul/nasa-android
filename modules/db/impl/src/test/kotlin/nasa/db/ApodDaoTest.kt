package nasa.db

import alakazam.test.db.RoomDatabaseRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import nasa.db.apod.ApodDao
import nasa.db.apod.ApodEntity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class ApodDaoTest {
  @get:Rule
  val databaseRule = RoomDatabaseRule(RoomNasaDatabase::class)

  private lateinit var apodDao: ApodDao

  @Before
  fun before() {
    apodDao = databaseRule.database.apodDao()
  }

  @Test
  fun `Insert and observe dates`() = runTest {
    apodDao.observeDates().test {
      // Empty initially
      assertEquals(expected = emptyList(), actual = awaitItem())

      // Insert entity from 2024
      apodDao.insert(imageEntity(DATE_2024))
      assertEquals(expected = listOf(DATE_2024), actual = awaitItem())

      // Insert entity from 1995
      apodDao.insert(videoEntity(DATE_1995))
      assertEquals(expected = listOf(DATE_1995, DATE_2024), actual = awaitItem())

      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Insert and get entities`() = runTest {
    // initially fail to fetch
    assertNull(apodDao.get(DATE_2024))

    // Insert different date, still fails to fetch
    apodDao.insert(imageEntity(DATE_1995))
    assertNull(apodDao.get(DATE_2024))

    // Insert 2024 date, fetches successfully
    val video = videoEntity(DATE_2024)
    apodDao.insert(video)
    assertEquals(expected = video, actual = apodDao.get(DATE_2024))
  }

  @Test
  fun `Overwrite entity`() = runTest {
    val initial = imageEntity(DATE_2024)
    apodDao.insert(initial)
    assertEquals(expected = initial, actual = apodDao.get(DATE_2024))

    // Then the initial one is overwritten
    val copy = initial.copy(title = "Something else in this field")
    apodDao.insert(copy)
    assertEquals(expected = copy, actual = apodDao.get(DATE_2024))
  }

  private fun imageEntity(date: LocalDate) = ApodEntity(
    date = date,
    title = "Foobar",
    explanation = "Here be dragons",
    mediaType = "Image",
    copyright = "Some person",
    url = "https://website.com/sd",
    hdUrl = "https://website.com/hd",
    thumbnailUrl = null,
  )

  private fun videoEntity(date: LocalDate) = ApodEntity(
    date = date,
    title = "Video",
    explanation = "Something goes here",
    mediaType = "Video",
    copyright = "JQ Public",
    url = "https://website.com/video",
    hdUrl = null,
    thumbnailUrl = "https://website.com/thumbnail",
  )
}
