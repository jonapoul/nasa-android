package apod.data.api

import apod.core.model.ApodMediaType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApodResponseModel(
  // Date of image. Included in response because of default values
  @SerialName("date")
  val date: LocalDate,

  // The title of the image
  @SerialName("title")
  val title: String,

  // The supplied text explanation of the image
  @SerialName("explanation")
  val explanation: String,

  // The type of media (data) returned. May either be 'image' or 'video' depending on content
  @SerialName("media_type")
  val mediaType: ApodMediaType,

  // The name(s) of the copyright holder
  @SerialName("copyright")
  val copyright: String?,

  // The URL of the APOD image or video. Null only if mediaType is "other".
  @SerialName("url")
  val url: String?,

  // The URL for any high-resolution image for that day. Returned regardless of 'hd' param
  // setting but will be omitted in the response IF it does not exist originally at APOD
  @SerialName("hdurl")
  val hdUrl: String?,

  // The URL of the image/video thumbnail
  @SerialName("thumbnail_url")
  val thumbnailUrl: String?,
)
