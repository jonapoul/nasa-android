package nasa.about.ui

import alakazam.android.core.IBuildConfig
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import nasa.about.data.BuildConfigProvider
import nasa.about.data.BuildConfigProviderImpl
import nasa.about.data.GithubApi
import nasa.core.model.ThemeType
import nasa.core.ui.NasaTheme
import nasa.test.NasaTestActivity
import nasa.test.runTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AboutScreenTest {
  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeRule = createAndroidComposeRule<NasaTestActivity>()

  private lateinit var testGithubApi: TestGithubApi

  @BindValue lateinit var buildConfigProvider: BuildConfigProvider
  @BindValue lateinit var buildConfig: IBuildConfig
  @BindValue lateinit var githubApi: GithubApi

  @Before
  fun before() {
    buildConfig = TestBuildConfig
    buildConfigProvider = BuildConfigProviderImpl(buildConfig.versionName, buildConfig.buildTime)
    testGithubApi = TestGithubApi()
    githubApi = testGithubApi

    hiltRule.inject()
    Intents.init()
  }

  @After
  fun after() {
    Intents.release()
  }

  @Test
  fun clickingReportIssuesLaunchesBrowser() = composeRule.runTest {
    // Given
    setContent()

    // When
    onNodeWithTag(Tags.ReportButton).performClick()

    // Then
    val expectedIntent = Matchers.allOf(
      IntentMatchers.hasAction(Intent.ACTION_VIEW),
      IntentMatchers.hasData(Uri.parse("${TestBuildConfig.repoUrl}/issues/new")),
    )
    Intents.intended(expectedIntent)
  }

  private fun ComposeContentTestRule.setContent() {
    setContent {
      val navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      NasaTheme(ThemeType.Light) { AboutScreen(navController) }
    }
  }
}
