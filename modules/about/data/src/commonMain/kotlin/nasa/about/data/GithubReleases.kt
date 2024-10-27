package nasa.about.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(GithubReleasesSerializer::class)
sealed interface GithubReleases {
  @Serializable
  data class Valid(
    val releases: List<GithubReleaseModel>,
  ) : GithubReleases

  // Returned if the repo that we're querying is private
  @Serializable
  data class Invalid(
    @SerialName("message")
    val message: String,
  ) : GithubReleases
}
