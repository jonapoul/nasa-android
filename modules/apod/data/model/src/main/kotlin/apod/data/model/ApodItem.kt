package apod.data.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
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
