package nasa.android.app

import alakazam.android.core.IBuildConfig
import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import nasa.about.ui.AboutScreen
import nasa.android.BuildConfig
import nasa.apod.grid.ui.ApodGridScreen
import nasa.apod.single.ui.ApodFullScreen
import nasa.apod.single.ui.ApodSingleScreen
import nasa.core.http.DownloadProgressInterceptor
import nasa.core.http.DownloadProgressStateHolder
import nasa.core.http.buildOkHttp
import nasa.core.model.ApiKey
import nasa.core.model.IMAGE_CACHE_DIR
import nasa.gallery.search.ui.GallerySearchScreen
import nasa.home.ui.HomeScreen
import nasa.licenses.ui.LicensesScreen
import nasa.nav.NavScreens
import nasa.settings.ui.SettingsScreen
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class NasaApplication : Application(), ImageLoaderFactory {
  @Inject
  lateinit var bc: IBuildConfig

  @Inject
  lateinit var apiKeyManager: ApiKeyManager

  @Inject
  lateinit var downloadProgressStateHolder: DownloadProgressStateHolder

  override fun onCreate() {
    super.onCreate()
    Timber.plant(NasaTree())

    Timber.i("onCreate")
    Timber.d("name=${bc.versionName} code=${bc.versionCode} time=${bc.buildTime}")
    Timber.d("manufacturer=${bc.manufacturer} model=${bc.model} os=${bc.os} platform=${bc.platform}")

    Timber.v("Setting API key...")
    // API_KEY can be null, based on gradle script logic
    @Suppress("UNNECESSARY_SAFE_CALL")
    val buildKey = BuildConfig.API_KEY?.let(::ApiKey)
    apiKeyManager.set(buildKey)

    Timber.v("Registering screens...")
    ScreenRegistry {
      register<NavScreens.Home> { HomeScreen() }
      register<NavScreens.ApodSingle> { ApodSingleScreen(it.config) }
      register<NavScreens.ApodGrid> { ApodGridScreen(it.config) }
      register<NavScreens.ApodFullScreen> { ApodFullScreen(it.item) }
      register<NavScreens.Gallery> { GallerySearchScreen() }
      register<NavScreens.About> { AboutScreen() }
      register<NavScreens.Licenses> { LicensesScreen() }
      register<NavScreens.Settings> { SettingsScreen() }
    }

    Timber.v("Building ImageLoader...")
    val imageLoader = newImageLoader()
    Coil.setImageLoader(imageLoader)
    Timber.v("Done!")
  }

  override fun newImageLoader(): ImageLoader {
    val progressInterceptor = DownloadProgressInterceptor(downloadProgressStateHolder)
    val client = buildOkHttp(bc.debug, progressInterceptor) { Timber.tag("COIL").v(it) }
    return ImageLoader.Builder(this)
      .memoryCache {
        MemoryCache.Builder(this)
          .maxSizePercent(percent = 0.2) // 20%, not 0.2%
          .build()
      }
      .diskCache {
        DiskCache.Builder()
          .directory(cacheDir.resolve(IMAGE_CACHE_DIR))
          .maxSizeBytes(size = 100 * 1024 * 1024) // 100MB
          .build()
      }
      .respectCacheHeaders(true)
      .networkObserverEnabled(true)
      .okHttpClient(client)
      .apply { if (bc.debug) logger(DebugLogger()) }
      .build()
  }
}
