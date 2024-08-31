package nasa.home.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import nasa.apod.data.repo.SingleApodRepository
import nasa.apod.data.repo.SingleLoadResult
import nasa.core.android.UrlOpener
import nasa.core.http.ApiUsage
import nasa.core.http.ApiUsageStateHolder
import nasa.core.model.ApiKey
import nasa.core.model.NASA_API_URL
import nasa.gallery.data.api.CollectionItemLink.Relation.Preview
import nasa.gallery.data.repo.GallerySearchRepository
import nasa.gallery.data.repo.SearchResult
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Year
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
  private val stateHolder: ApiUsageStateHolder,
  private val urlOpener: UrlOpener,
  private val provider: ApiKey.Provider,
  private val apodRepository: SingleApodRepository,
  private val galleryRepository: GallerySearchRepository,
) : ViewModel() {
  val apodThumbnailUrl: Flow<String?> get() = provider.observe().map { key -> fetchApodUrl(key) }

  val galleryThumbnailUrl: Flow<String?> get() = flow { emit(fetchGalleryUrl()) }

  internal fun apiUsage(): Flow<ApiUsageState> = combine(stateHolder.state, provider.observe(), ::toState)

  fun registerForApiKey() = urlOpener.openUrl(NASA_API_URL)

  private fun toState(usage: ApiUsage?, key: ApiKey?): ApiUsageState = when {
    key == null -> ApiUsageState.NoApiKey

    key == ApiKey.DEMO -> if (usage == null) {
      ApiUsageState.DemoKeyNoUsage
    } else {
      ApiUsageState.DemoKeyHasUsage(usage.remaining, usage.upperLimit)
    }

    usage == null -> ApiUsageState.RealKeyNoUsage

    else -> ApiUsageState.RealKeyHasUsage(usage.remaining, usage.upperLimit)
  }

  private suspend fun fetchApodUrl(key: ApiKey?): String? = if (key == null) {
    null
  } else {
    val result = apodRepository.loadToday(key)
    Timber.v("fetchApodUrl = $result")
    if (result is SingleLoadResult.Success) {
      result.item.thumbnailUrl ?: result.item.url
    } else {
      null
    }
  }

  private suspend fun fetchGalleryUrl(): String? {
    val config = FilterConfig(
      yearStart = Year.Maximum,
      mediaTypes = MediaTypes(MediaType.Image),
    )
    val result = galleryRepository.search(config, pageNumber = 1, pageSize = 1)
    Timber.v("fetchGalleryUrl = $result")
    return if (result is SearchResult.Success) {
      result.pagedResults
        .firstOrNull()
        ?.links
        ?.firstOrNull { it.rel == Preview }
        ?.url
        ?.toString()
    } else {
      null
    }
  }
}
