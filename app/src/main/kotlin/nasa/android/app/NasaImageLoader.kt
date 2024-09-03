package nasa.android.app

import alakazam.android.core.IBuildConfig
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import nasa.core.http.factories.buildOkHttp
import nasa.core.http.progress.DownloadProgressInterceptor
import nasa.core.http.progress.DownloadProgressStateHolder
import nasa.core.model.IMAGE_CACHE_DIR
import nasa.core.model.megabytes
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NasaImageLoader @Inject constructor(
  private val context: Context,
  private val buildConfig: IBuildConfig,
  private val downloadProgressStateHolder: DownloadProgressStateHolder,
) : ImageLoaderFactory {
  override fun newImageLoader(): ImageLoader {
    val progressInterceptor = DownloadProgressInterceptor(downloadProgressStateHolder)
    val client = buildOkHttp(buildConfig.debug, progressInterceptor) { Timber.tag("COIL").v(it) }
    return ImageLoader
      .Builder(context)
      .memoryCache {
        MemoryCache
          .Builder(context)
          .maxSizePercent(percent = 0.2) // 20%, not 0.2%
          .build()
      }.diskCache {
        DiskCache
          .Builder()
          .directory(context.cacheDir.resolve(IMAGE_CACHE_DIR))
          .maxSizeBytes(size = 100.megabytes.toBytes())
          .build()
      }.respectCacheHeaders(true)
      .networkObserverEnabled(true)
      .okHttpClient(client)
      .apply { if (buildConfig.debug) logger(DebugLogger()) }
      .build()
  }
}
