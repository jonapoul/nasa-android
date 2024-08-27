package nasa.about.data

import alakazam.kotlin.core.IODispatcher
import alakazam.test.core.standardDispatcher
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import nasa.test.MockWebServerRule
import nasa.test.getResourceAsText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GithubRepositoryTest {
  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var githubRepository: GithubRepository
  private lateinit var githubApi: GithubApi
  private lateinit var buildConfig: BuildConfigProvider

  @Before
  fun before() {
    buildConfig = BuildConfigProviderImpl(
      versionName = "1.2.3",
      buildTime = Instant.fromEpochMilliseconds(1710786854286L), // Mon Mar 18 2024 18:34:14
    )
    githubApi = webServerRule.buildApi(json = GithubJson)
  }

  @Test
  fun `Update available from three returned`() = runTest {
    // Given
    buildRepo()
    val threeReleasesJson = readJson(filename = "new-release.json")
    webServerRule.enqueue(threeReleasesJson)

    // When
    val state = githubRepository.fetchLatestRelease()

    // Then
    assertEquals(expected = "1.2.3", actual = buildConfig.versionName)
    assertEquals(
      actual = state,
      expected = LatestReleaseState.UpdateAvailable(
        release = GithubReleaseModel(
          versionName = "v2.3.4",
          publishedAt = Instant.parse("2024-03-30T09:57:02Z"),
          htmlUrl = "https://github.com/jonapoul/nasa-android/releases/tag/2.3.4",
        ),
      ),
    )
  }

  @Test
  fun `No new updates`() = runTest {
    // Given
    buildRepo()
    val threeReleasesJson = readJson(filename = "no-new-update.json")
    webServerRule.enqueue(threeReleasesJson)

    // When
    val state = githubRepository.fetchLatestRelease()

    // Then
    assertEquals(expected = "1.2.3", actual = buildConfig.versionName)
    assertEquals(actual = state, expected = LatestReleaseState.NoNewUpdate)
  }

  @Test
  fun `No releases`() = runTest {
    // Given
    buildRepo()
    val emptyArrayResponse = "[]"
    webServerRule.enqueue(emptyArrayResponse)

    // When
    val state = githubRepository.fetchLatestRelease()

    // Then
    assertEquals(actual = state, expected = LatestReleaseState.NoReleases)
  }

  @Test
  fun `Private repo`() = runTest {
    // Given
    buildRepo()
    val privateRepoJson = readJson(filename = "not-found.json")
    webServerRule.enqueue(privateRepoJson)

    // When
    val state = githubRepository.fetchLatestRelease()

    // Then
    assertEquals(actual = state, expected = LatestReleaseState.PrivateRepo)
  }

  @Test
  fun `Invalid JSON response`() = runTest {
    // Given
    buildRepo()
    val jsonResponse = """
      {
        "foo" : "bar"
      }
    """.trimIndent()
    webServerRule.enqueue(jsonResponse)

    // When
    val state = githubRepository.fetchLatestRelease()

    // Then
    assertIs<LatestReleaseState.Failure>(state)
    assertTrue(state.errorMessage.contains("Failed decoding JSON"), state.errorMessage)
  }

  @Test
  fun `Network failure`() = runTest {
    // Given
    buildRepo()
    githubApi = mockk { coEvery { getAppReleases() } throws IOException("Network broke!") }
    buildRepo()

    // When
    val state = githubRepository.fetchLatestRelease()

    // Then
    assertIs<LatestReleaseState.Failure>(state)
    assertTrue(state.errorMessage.contains("IO failure"), state.errorMessage)
  }

  private fun readJson(filename: String): String = getResourceAsText(filename)

  private fun TestScope.buildRepo() {
    githubRepository = GithubRepository(
      io = IODispatcher(standardDispatcher),
      api = githubApi,
      buildConfig = buildConfig,
    )
  }
}
