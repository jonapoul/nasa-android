package nasa.about.ui

import nasa.about.data.GithubApi
import nasa.about.data.GithubReleases

internal class TestGithubApi(var releases: GithubReleases? = null) : GithubApi {
  override suspend fun getAppReleases(): GithubReleases = requireNotNull(releases)
}
