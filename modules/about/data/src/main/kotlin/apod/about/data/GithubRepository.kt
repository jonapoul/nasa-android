package apod.about.data

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubRepository @Inject internal constructor(
  private val io: IODispatcher,
  private val api: GithubApi,
  private val buildConfig: BuildConfigProvider,
) {
  suspend fun fetchLatestRelease(): LatestReleaseState {
    try {
      val response = withContext(io) { api.getAppReleases() }
      if (response !is GithubReleases.Valid) {
        return LatestReleaseState.PrivateRepo
      }

      val releases = response.releases
      val latest = releases.maxByOrNull { it.publishedAt }
      return when {
        releases.isEmpty() || latest == null ->
          LatestReleaseState.NoReleases

        buildConfig.versionName == latest.clippedVersion() ->
          LatestReleaseState.NoNewUpdate

        else -> {
          if (latest.publishedAt > buildConfig.buildTime) {
            LatestReleaseState.UpdateAvailable(latest)
          } else {
            // It's the same version as current (or earlier?)
            LatestReleaseState.NoNewUpdate
          }
        }
      }
    } catch (e: Exception) {
      return e.toFailure()
    }
  }

  private fun Exception.toFailure(): LatestReleaseState.Failure {
    val errorMessage = when (this) {
      is SerializationException -> "Failed decoding JSON: $message"
      is HttpException -> "HTTP error ${code()}: ${message()}"
      is IOException -> "IO failure: $message"
      else -> "Other error - ${javaClass.simpleName}: $message"
    }
    return LatestReleaseState.Failure(errorMessage)
  }

  private fun GithubReleaseModel.clippedVersion(): String = versionName.replace(ANY_LETTER, replacement = "")

  private companion object {
    val ANY_LETTER = "[a-zA-z]".toRegex()
  }
}
