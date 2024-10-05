package nasa.db

import alakazam.test.core.standardDispatcher
import app.cash.turbine.test
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApodDaoTest {
  @get:Rule
  val temporaryFolder = TemporaryFolder()

  private lateinit var database: NasaDatabase
  private lateinit var apodDao: ApodDao

  private fun TestScope.before() {
    val builder = getDatabaseBuilder(temporaryFolder.newFolder())
    database = NasaDatabase.build(builder, standardDispatcher)
    apodDao = database.apodDao()
  }

  @After
  fun after() {
    database.close()
  }

  @Test
  fun `Insert and observe dates`() = runTest {
    before()
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
    before()

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
    before()
    val initial = imageEntity(DATE_2024)
    apodDao.insert(initial)
    assertEquals(expected = initial, actual = apodDao.get(DATE_2024))

    // Then the initial one is overwritten
    val copy = initial.copy(title = "Something else in this field")
    apodDao.insert(copy)
    assertEquals(expected = copy, actual = apodDao.get(DATE_2024))
  }
}
