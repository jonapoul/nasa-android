package apod.data.repo

import apod.data.api.ApodResponseModel
import apod.data.db.ApodEntity
import apod.data.model.ApodItem

internal fun ApodEntity.toItem(): ApodItem = ApodItem(
  date = date,
  title = title,
  explanation = explanation,
  mediaType = mediaType,
  copyright = copyright,
  url = url,
  hdUrl = hdUrl,
  thumbnailUrl = thumbnailUrl,
)

internal fun ApodResponseModel.toItem(): ApodItem = ApodItem(
  date = date,
  title = title,
  explanation = explanation,
  mediaType = mediaType,
  copyright = copyright,
  url = url,
  hdUrl = hdUrl,
  thumbnailUrl = thumbnailUrl,
)

internal fun ApodItem.toEntity(): ApodEntity = ApodEntity(
  date = date,
  title = title,
  explanation = explanation,
  mediaType = mediaType,
  copyright = copyright,
  url = url,
  hdUrl = hdUrl,
  thumbnailUrl = thumbnailUrl,
)
