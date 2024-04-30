package apod.about.data

import retrofit2.http.GET

/**
 * From https://docs.github.com/en/rest/releases/releases?apiVersion=2022-11-28#list-releases
 */
interface GithubApi {
  @GET("/repos/jonapoul/apod-android/releases")
  suspend fun getAppReleases(): GithubReleases
}
