package nasa.db.apod

import kotlinx.datetime.LocalDate

interface ApodEntity {
  val date: LocalDate
  val title: String
  val explanation: String
  val mediaType: String
  val copyright: String?
  val url: String?
  val hdUrl: String?
  val thumbnailUrl: String?

  fun interface Factory {
    fun build(
      date: LocalDate,
      title: String,
      explanation: String,
      mediaType: String,
      copyright: String?,
      url: String?,
      hdUrl: String?,
      thumbnailUrl: String?,
    ): ApodEntity
  }
}
