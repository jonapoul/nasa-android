package apod.data.repo

import apod.data.model.ApodItem

sealed interface LoadResult {
  data class Success(val item: ApodItem) : LoadResult
  data class Failure(val reason: String) : LoadResult
}
