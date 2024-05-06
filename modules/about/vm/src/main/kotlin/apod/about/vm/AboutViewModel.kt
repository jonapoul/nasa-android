package apod.about.vm

import alakazam.android.core.IBuildConfig
import alakazam.kotlin.core.exhaustive
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apod.about.data.GithubRepository
import apod.about.data.LatestReleaseState
import apod.core.url.UrlOpener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaInstant
import timber.log.Timber
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject internal constructor(
  private val buildConfig: IBuildConfig,
  private val githubRepository: GithubRepository,
  private val urlOpener: UrlOpener,
) : ViewModel() {
  val buildState: StateFlow<BuildState> = MutableStateFlow(buildState())

  private val mutableCheckUpdatesState = MutableStateFlow<CheckUpdatesState>(CheckUpdatesState.Inactive)
  val checkUpdatesState: StateFlow<CheckUpdatesState> = mutableCheckUpdatesState.asStateFlow()

  private var checkUpdatesJob: Job? = null

  fun openRepo() {
    openUrl(url = buildConfig.repoUrl)
  }

  fun reportIssues() {
    openUrl(url = "${buildConfig.repoUrl}/issues/new")
  }

  fun openUrl(url: String) {
    Timber.v("openUrl $url")
    urlOpener.openUrl(url)
  }

  fun fetchLatestRelease() {
    Timber.v("fetchLatestRelease")
    checkUpdatesJob?.cancel()
    mutableCheckUpdatesState.update { CheckUpdatesState.Checking }

    checkUpdatesJob = viewModelScope.launch {
      val state = githubRepository.fetchLatestRelease()
      mutableCheckUpdatesState.update {
        when (state) {
          LatestReleaseState.NoNewUpdate -> CheckUpdatesState.NoUpdateFound
          LatestReleaseState.NoReleases -> CheckUpdatesState.NoUpdateFound
          LatestReleaseState.PrivateRepo -> CheckUpdatesState.Failed(cause = "Repo inaccessible")
          is LatestReleaseState.Failure -> CheckUpdatesState.Failed(state.errorMessage)
          is LatestReleaseState.UpdateAvailable -> CheckUpdatesState.UpdateFound(
            version = state.release.versionName,
            url = state.release.htmlUrl,
          )
        }.exhaustive
      }
    }
  }

  fun cancelUpdateCheck() {
    Timber.v("stopCheckingUpdates")
    checkUpdatesJob?.cancel()
    checkUpdatesJob = null
    mutableCheckUpdatesState.update { CheckUpdatesState.Inactive }
  }

  private fun buildState(): BuildState {
    val version = "${buildConfig.versionName} (${buildConfig.versionCode})"
    val zone = ZoneId.systemDefault()
    val zonedDateTime = buildConfig.buildTime.toJavaInstant().atZone(zone)
    return BuildState(
      buildVersion = version,
      buildDate = DateTimeFormatter.RFC_1123_DATE_TIME.format(zonedDateTime),
      sourceCodeRepo = buildConfig.repoName,
      year = zonedDateTime.year,
    )
  }
}
