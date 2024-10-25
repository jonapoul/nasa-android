package nasa.apod.model

import kotlinx.datetime.LocalDate

data class ApodItem(
  val date: LocalDate,
  val title: String,
  val explanation: String,
  val mediaType: ApodMediaType,
  val copyright: String?,
  val url: String,
  val hdUrl: String?,
  val thumbnailUrl: String?,
)
