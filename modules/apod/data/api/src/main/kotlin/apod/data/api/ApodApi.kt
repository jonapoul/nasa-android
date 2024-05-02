package apod.data.api

import apod.core.model.ApiKey
import kotlinx.datetime.LocalDate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Docs pulled from https://github.com/nasa/apod-api?tab=readme-ov-file#docs
 *
 * Query parameters:
 *      api_key:     Pull from https://api.nasa.gov/#signUp
 *      date:        A string in YYYY-MM-DD format indicating the date of the APOD image (example:
 *                   2014-11-03). Defaults to today's date. Must be after 1995-06-16, the first day
 *                   an APOD picture was posted. There are no images for tomorrow available through
 *                   this API.
 *      start_date:  A string in YYYY-MM-DD format indicating the start of a date range. All images
 *                   in the range from start_date to end_date will be returned in a JSON array.
 *                   Cannot be used with date.
 *      end_date:    A string in YYYY-MM-DD format indicating the end of a date range. If
 *                   start_date is specified without an end_date then end_date defaults to the
 *                   current date.
 *      count:       A positive integer, no greater than 100. If this is specified then count
 *                   randomly chosen images will be returned in a JSON array. Cannot be used in
 *                   conjunction with date or start_date and end_date.
 *      thumbs:      If set to true, the API returns URL of video thumbnail. If an APOD is not a
 *                   video, this parameter is ignored.
 *
 * There are a few extra params we can pass in, but this app doesn't need them ¯\_(ツ)_/¯
 */
interface ApodApi {
  @GET(ENDPOINT)
  suspend fun getToday(
    @Query("api_key") apiKey: ApiKey,
    @Query("thumbs") thumbs: Boolean = true,
  ): Response<ApodResponseModel>

  @GET(ENDPOINT)
  suspend fun getByDate(
    @Query("api_key") apiKey: ApiKey,
    @Query("date") date: LocalDate,
    @Query("thumbs") thumbs: Boolean = true,
  ): Response<ApodResponseModel>

  @GET(ENDPOINT)
  suspend fun getRandom(
    @Query("api_key") apiKey: ApiKey,
    @Query("count") count: Int = 1,
    @Query("thumbs") thumbs: Boolean = true,
  ): List<ApodResponseModel>

  @GET(ENDPOINT)
  suspend fun getRange(
    @Query("api_key") apiKey: ApiKey,
    @Query("start_date") startDate: LocalDate,
    @Query("end_date") endDate: LocalDate,
    @Query("thumbs") thumbs: Boolean = true,
  ): List<ApodResponseModel>

  private companion object {
    const val ENDPOINT = "/planetary/apod"
  }
}
