package nasa.apod.data.repo

import nasa.apod.data.api.ApodResponseModel
import nasa.apod.data.db.ApodEntity
import nasa.apod.model.ApodItem

internal fun ApodEntity.toItem(): ApodItem = ApodItem(
  date = date,
  title = title,
  explanation = explanation,
  mediaType = mediaType,
  copyright = copyright,
  url = url.orEmpty(),
  hdUrl = hdUrl,
  thumbnailUrl = thumbnailUrl,
)

internal fun ApodResponseModel.toItem(): ApodItem = ApodItem(
  date = date,
  title = title,
  explanation = explanation,
  mediaType = mediaType,
  copyright = copyright,
  url = url.orEmpty(),
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
