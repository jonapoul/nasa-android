package apod.android.app

import alakazam.android.core.IBuildConfig
import android.app.Application
import apod.about.ui.AboutScreen
import apod.android.BuildConfig
import apod.core.http.buildOkHttp
import apod.core.model.ApiKey
import apod.grid.ui.ApodGridScreen
import apod.licenses.ui.LicensesScreen
import apod.navigation.NavScreens
import apod.settings.ui.SettingsScreen
import apod.single.ui.ApodFullScreen
import apod.single.ui.ApodSingleScreen
import cafe.adriel.voyager.core.registry.ScreenRegistry
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ApodApplication : Application(), ImageLoaderFactory {
  @Inject
  lateinit var bc: IBuildConfig

  @Inject
  lateinit var apiKeyManager: ApiKeyManager

  override fun onCreate() {
    super.onCreate()
    Timber.plant(ApodTree())

    Timber.i("onCreate")
    Timber.d("name=${bc.versionName} code=${bc.versionCode} time=${bc.buildTime}")
    Timber.d("manufacturer=${bc.manufacturer} model=${bc.model} os=${bc.os} platform=${bc.platform}")

    // API_KEY can be null, based on gradle script logic
    @Suppress("UNNECESSARY_SAFE_CALL")
    val buildKey = BuildConfig.API_KEY?.let(::ApiKey)
    apiKeyManager.set(buildKey)

    ScreenRegistry {
      register<NavScreens.Apod> { ApodSingleScreen(it.config) }
      register<NavScreens.Grid> { ApodGridScreen(it.config) }
      register<NavScreens.FullScreen> { ApodFullScreen(it.item) }
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
    val client = buildOkHttp { Timber.tag("COIL").v(it) }
    return ImageLoader.Builder(this)
      .memoryCache {
        MemoryCache.Builder(this)
          .maxSizePercent(percent = 0.2) // 20%, not 0.2%
          .build()
      }
      .diskCache {
        DiskCache.Builder()
          .directory(cacheDir.resolve("image_cache"))
          .maxSizeBytes(size = 100 * 1024 * 1024) // 100MB
          .build()
      }
      .respectCacheHeaders(true)
      .networkObserverEnabled(true)
      .okHttpClient(client)
      .apply { if (BuildConfig.DEBUG) logger(DebugLogger()) }
      .build()
  }
}
