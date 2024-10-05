package nasa.db

import kotlinx.datetime.LocalDate

fun imageEntity(date: LocalDate) = ApodEntity(
  date = date,
  title = "Foobar",
  explanation = "Here be dragons",
  mediaType = "Image",
  copyright = "Some person",
  url = "https://website.com/sd",
  hdUrl = "https://website.com/hd",
  thumbnailUrl = null,
)

fun videoEntity(date: LocalDate) = ApodEntity(
  date = date,
  title = "Video",
  explanation = "Something goes here",
  mediaType = "Video",
  copyright = "JQ Public",
  url = "https://website.com/video",
  hdUrl = null,
  thumbnailUrl = "https://website.com/thumbnail",
)
