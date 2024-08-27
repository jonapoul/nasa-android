package nasa.db

import alakazam.kotlin.core.IODispatcher
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import nasa.db.apod.ApodEntity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class NasaDatabaseMigrationTest {
  private lateinit var db: RoomNasaDatabase

  @get:Rule
  val helper = MigrationTestHelper(
    InstrumentationRegistry.getInstrumentation(),
    RoomNasaDatabase::class.java,
    emptyList(),
    FrameworkSQLiteOpenHelperFactory(),
  )

  @After
  fun after() {
    if (::db.isInitialized) {
      db.close()
    }
  }

  @Test
  fun `Migrate v1 to latest`() = runTest {
    // Initialise at v1
    val dateInitial = LocalDate.parse("2024-01-02")
    helper.createDatabase(TEST_DB, version = 1).use { db ->
      // Create tables
      db.execSQL(CREATE_APOD_STATEMENT)
      db.execSQL(CREATE_GALLERY_STATEMENT)

      // Insert some data
      db.execSQL(
        """
          INSERT INTO apod
          (date,title,explanation,media_type,copyright,url,hdurl,thumbnail_url)
          VALUES
          ("$dateInitial","Hello world","blah blah","image",null,"https://url.com/image.jpg",null,null)
        """.trimIndent(),
      )
    }

    // Migrate to latest
    val migrated = helper.runMigrationsAndValidate(TEST_DB, RoomNasaDatabase.VERSION, validateDroppedTables = true)
    migrated.close()

    // Wrap in a DB object - Room will validate the tables internally
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val io = IODispatcher(StandardTestDispatcher(testScheduler))
    db = RoomNasaDatabase.build(context, io, TEST_DB)

    // Insert some new data
    val apod = db.apodDao()
    val dateNew = LocalDate.parse("2024-08-04")
    apod.insert(
      ApodEntity(
        date = dateNew,
        title = "abc",
        explanation = "xyz",
        mediaType = "video",
        copyright = "jkl",
        url = null,
        hdUrl = "https://url.com/image-hd.jpg",
        thumbnailUrl = null,
      ),
    )

    advanceUntilIdle()

    // The initial APOD entity is still there
    assertEquals(
      actual = apod.get(dateInitial),
      expected = ApodEntity(
        date = dateInitial,
        title = "Hello world",
        explanation = "blah blah",
        mediaType = "image",
        copyright = null,
        url = "https://url.com/image.jpg",
        hdUrl = null,
        thumbnailUrl = null,
      ),
    )

    // And there are now two in the table
    val dates = apod.observeDates().first().sorted()
    assertEquals(expected = listOf(dateInitial, dateNew), actual = dates)
  }

  private companion object {
    const val TEST_DB = "test.db"

    val CREATE_APOD_STATEMENT = """
      CREATE TABLE IF NOT EXISTS apod (
      date TEXT NOT NULL,
      title TEXT NOT NULL,
      explanation TEXT NOT NULL,
      media_type TEXT NOT NULL,
      copyright TEXT,
      url TEXT,
      hdurl TEXT,
      thumbnail_url TEXT,
      PRIMARY KEY(date))
    """.trimIndent()

    const val CREATE_GALLERY_STATEMENT = "CREATE TABLE IF NOT EXISTS gallery (date TEXT NOT NULL, PRIMARY KEY(date))"
  }
}
