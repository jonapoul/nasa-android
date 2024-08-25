package nasa.settings.ui

import alakazam.android.core.Toaster
import alakazam.kotlin.core.IODispatcher
import alakazam.test.core.CoroutineRule
import alakazam.test.db.RoomDatabaseRule
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import nasa.core.android.UrlOpener
import nasa.core.model.NASA_API_URL
import nasa.core.model.bytes
import nasa.db.NasaDatabaseDelegate
import nasa.db.RoomNasaDatabase
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {
  @get:Rule
  val databaseRule = RoomDatabaseRule(RoomNasaDatabase::class, allowMainThread = true)

  @get:Rule
  val coroutineRule = CoroutineRule()

  @get:Rule
  val timberRule = TimberTestRule.logAllWhenTestFails()!!

  // real
  private lateinit var viewModel: SettingsViewModel
  private lateinit var imageCache: ImageCache
  private lateinit var databaseClearer: DatabaseClearer

  // mock
  private lateinit var urlOpener: UrlOpener
  private lateinit var toaster: Toaster

  @Before
  fun before() {
    urlOpener = mockk(relaxed = true)
    toaster = mockk(relaxed = true)
    imageCache = ImageCache(ApplicationProvider.getApplicationContext())
    imageCache.cacheDir.mkdirs()

    databaseClearer = DatabaseClearer(
      io = IODispatcher(coroutineRule.dispatcher),
      database = NasaDatabaseDelegate(databaseRule.database),
    )

    viewModel = SettingsViewModel(
      urlOpener = urlOpener,
      imageCache = imageCache,
      databaseClearer = databaseClearer,
      toaster = toaster,
    )
  }

  @Test
  fun `Register for key`() = runTest {
    viewModel.registerForApiKey()
    verify { urlOpener.openUrl(NASA_API_URL) }
    confirmVerified(urlOpener)
  }

  @Test
  fun `Clear cache of files`() = runTest {
    viewModel.cacheSize.test {
      // Given the cache is empty
      assertEquals(0.bytes, awaitItem())

      // When we write some files
      imageCache.writeBytesToFile(filename = "a.txt", numBytes = 123)
      viewModel.refreshCacheSize()

      // Then
      assertEquals(123.bytes, awaitItem())

      // When we clear
      viewModel.clearCache()

      // Then we have no cache, and a toast was shown
      assertEquals(0.bytes, awaitItem())
      testScheduler.advanceUntilIdle()
      coVerify { toaster.coToast("Successfully cleared cache") }
      confirmVerified(toaster)
    }
  }
}
