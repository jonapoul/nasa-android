package apod.data.repo

import apod.core.model.ApodItem
import apod.data.api.ApodResponseModel
import apod.data.db.ApodEntity

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
