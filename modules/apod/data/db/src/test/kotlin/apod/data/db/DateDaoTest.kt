package apod.data.db

import alakazam.test.db.RoomDatabaseRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class DateDaoTest {
  @get:Rule
  val databaseRule = RoomDatabaseRule(ApodDatabase::class)

  private lateinit var dateDao: DateDao

  @Before
  fun before() {
    dateDao = databaseRule.database.dateDao()
  }

  @Test
  fun `Insert and get entities`() = runTest {
    // initially fail to fetch
    assertNull(dateDao.get(DATE_2024))

    // Insert different date, still fails to fetch
    dateDao.insert(DateEntity(DATE_1995, exists = true))
    assertNull(dateDao.get(DATE_2024))

    // Insert 2024 date, fetches successfully
    val video = DateEntity(DATE_2024, exists = true)
    dateDao.insert(video)
    assertEquals(expected = video, actual = dateDao.get(DATE_2024))
  }

  @Test
  fun `Overwrite entity`() = runTest {
    val initial = DateEntity(DATE_2024, exists = true)
    dateDao.insert(initial)
    assertEquals(expected = initial, actual = dateDao.get(DATE_2024))

    // Then the initial one is overwritten
    val copy = initial.copy(exists = false)
    dateDao.insert(copy)
    assertEquals(expected = copy, actual = dateDao.get(DATE_2024))
  }
}
