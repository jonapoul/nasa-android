package nasa.home.vm

import alakazam.android.core.UrlOpener
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import nasa.apod.data.repo.SingleApodRepository
import nasa.apod.data.repo.SingleLoadResult
import nasa.core.http.usage.ApiUsage
import nasa.core.http.usage.ApiUsageStateHolder
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
  private val apiUsageStateHolder: ApiUsageStateHolder,
  private val urlOpener: UrlOpener,
  private val apiKeyProvider: ApiKey.Provider,
  private val apodRepository: SingleApodRepository,
  private val galleryRepository: GallerySearchRepository,
) : ViewModel() {
  val apodThumbnailUrl: StateFlow<String?> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val key by apiKeyProvider.observe().collectAsState(initial = null)
    var apodUrl by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(key) { apodUrl = fetchApodUrl(key) }
    apodUrl
  }

  val galleryThumbnailUrl: StateFlow<String?> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    var url by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) { url = fetchGalleryUrl() }
    url
  }

  fun apiUsage(): StateFlow<ApiUsageState> = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    val usageState by apiUsageStateHolder.collectAsState()
    val usage = usageState
    val key by apiKeyProvider.observe().collectAsState(initial = null)
    when (key) {
      null -> ApiUsageState.NoApiKey
      ApiKey.DEMO -> demoKeyState(usage)
      else -> realKeyState(usage)
    }
  }

  fun registerForApiKey() = urlOpener.openUrl(NASA_API_URL)

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

  private fun demoKeyState(usage: ApiUsage?) = if (usage == null) {
    ApiUsageState.DemoKeyNoUsage
  } else {
    ApiUsageState.DemoKeyHasUsage(usage.remaining, usage.upperLimit)
  }

  private fun realKeyState(usage: ApiUsage?) = if (usage == null) {
    ApiUsageState.RealKeyNoUsage
  } else {
    ApiUsageState.RealKeyHasUsage(usage.remaining, usage.upperLimit)
  }
}
