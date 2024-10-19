package nasa.db

import alakazam.kotlin.core.IODispatcher
import alakazam.test.core.standardDispatcher
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class NasaDatabaseMigrationTest {
  private lateinit var db: NasaDatabase
  private val dbFile = File.createTempFile("migration-test", ".db")

  init {
    dbFile.deleteOnExit()
  }

  @get:Rule
  val helper = MigrationTestHelper(
    schemaDirectoryPath = TestBuildConfig.SCHEMAS_PATH.toPath(),
    databasePath = dbFile.toPath(),
    driver = BundledSQLiteDriver(),
    databaseClass = NasaDatabase::class,
    autoMigrationSpecs = emptyList(),
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
    val connection = helper.createDatabase(version = 1)
    with(connection) {
      // Create tables
      execSQL(CREATE_APOD_STATEMENT)
      execSQL(CREATE_GALLERY_STATEMENT)

      // Insert some data
      execSQL(
        """
          INSERT INTO apod
          (date,title,explanation,media_type,copyright,url,hdurl,thumbnail_url)
          VALUES
          ("$dateInitial","Hello world","blah blah","image",null,"https://url.com/image.jpg",null,null)
        """.trimIndent(),
      )
    }

    // Migrate to latest
    val migrated = helper.runMigrationsAndValidate(NasaDatabase.VERSION, migrations = emptyList())
    migrated.close()

    // Wrap in a DB object - Room will validate the tables internally
    val io = IODispatcher(standardDispatcher)
    val builder = getDatabaseBuilder(dbFile.parentFile, dbFile)
    db = NasaDatabase.build(builder, io)

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
