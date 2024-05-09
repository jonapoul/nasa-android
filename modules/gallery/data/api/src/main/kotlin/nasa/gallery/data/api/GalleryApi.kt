package nasa.gallery.data.api

import kotlinx.serialization.json.JsonObject
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.NasaId
import nasa.gallery.model.Year
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Docs adapted from https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf
 */
interface GalleryApi {
  /**
   * At least one parameter is required, but all individual parameters are optional.
   */
  @GET("search")
  suspend fun search(
    // Free text search terms to compare to all indexed metadata.
    @Query("q") query: String? = null,

    // NASA center which published the media.
    @Query("center") center: String? = null,

    // Terms to search for in "Description" fields.
    @Query("description") description: String? = null,

    // Terms to search for in "508 Description" fields.
    @Query("description_508") description508: String? = null,

    // Terms to search for in "Keywords" fields.
    @Query("keywords") keywords: Keywords? = null,

    // Terms to search for in "Location" fields.
    @Query("location") location: String? = null,

    // Media types to restrict the search to. Available types: ["image", "video", "audio"].
    @Query("media_type") mediaTypes: MediaTypes? = null,

    // The media asset’s NASA ID.
    @Query("nasa_id") nasaId: NasaId? = null,

    // Page number, starting at 1, of results to get.
    @Query("page") page: Int? = null,

    // Number of results per page. Default: 100.
    @Query("page_size") pageSize: Int? = null,

    // The primary photographer’s name.
    @Query("photographer") photographer: String? = null,

    // A secondary photographer/videographer’s name.
    @Query("secondary_creator") secondaryCreator: String? = null,

    // Terms to search for in "Title" fields.
    @Query("title") title: String? = null,

    // The start year for results. Format: YYYY.
    @Query("year_start") yearStart: Year? = null,

    // The end year for results. Format: YYYY.
    @Query("year_end") yearEnd: Year? = null,
  ): Response<SearchResponse.Success>

  @GET("asset/{nasa_id}")
  suspend fun getAssetManifest(
    // The media asset’s NASA ID.
    @Path("nasa_id") nasaId: NasaId,
  ): Response<ManifestResponse.Success>

  @GET("metadata/{nasa_id}")
  suspend fun locateMetadata(
    // The media asset’s NASA ID.
    @Path("nasa_id") nasaId: NasaId,
  ): Response<LocateResponse.Success>

  @GET("captions/{nasa_id}")
  suspend fun locateCaptions(
    // The video asset’s NASA ID.
    @Path("nasa_id") nasaId: NasaId,
  ): Response<LocateResponse.Success>

  @GET
  suspend fun getCollection(
    @Url url: String,
  ): Response<List<String>>

  @GET
  suspend fun getMetadata(
    @Url url: String,
  ): Response<JsonObject>
}
