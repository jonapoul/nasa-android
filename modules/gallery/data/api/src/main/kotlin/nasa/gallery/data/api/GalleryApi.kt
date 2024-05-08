package nasa.gallery.data.api

import nasa.core.model.ApiKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryApi {
  @GET(ENDPOINT)
  suspend fun getToday(
    @Query("api_key") apiKey: ApiKey,
    @Query("thumbs") thumbs: Boolean = true,
  ): Response<GalleryResponseModel>

  private companion object {
    const val ENDPOINT = "/planetary/apod"
  }
}
