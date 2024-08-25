package nasa.android.app

import alakazam.android.core.IBuildConfig
import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import coil.Coil
import dagger.hilt.android.HiltAndroidApp
import nasa.about.nav.AboutNavScreen
import nasa.about.ui.AboutScreen
import nasa.android.BuildConfig
import nasa.apod.nav.ApodFullScreenNavScreen
import nasa.apod.nav.ApodGridNavScreen
import nasa.apod.nav.ApodSingleNavScreen
import nasa.apod.ui.grid.ApodGridScreen
import nasa.apod.ui.single.ApodFullScreen
import nasa.apod.ui.single.ApodSingleScreen
import nasa.core.android.PreferencesApiKeyProvider
import nasa.core.model.ApiKey
import nasa.gallery.nav.GalleryImageNavScreen
import nasa.gallery.nav.GallerySearchNavScreen
import nasa.gallery.ui.image.GalleryImageScreen
import nasa.gallery.ui.search.GallerySearchScreen
import nasa.home.nav.HomeNavScreen
import nasa.home.ui.HomeScreen
import nasa.licenses.nav.LicensesNavScreen
import nasa.licenses.ui.LicensesScreen
import nasa.settings.nav.SettingsNavScreen
import nasa.settings.ui.SettingsScreen
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class NasaApplication : Application() {
  @Inject
  lateinit var bc: IBuildConfig

  @Inject
  lateinit var apiKeyProvider: PreferencesApiKeyProvider

  @Inject
  lateinit var imageLoader: NasaImageLoader

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
    apiKeyProvider.set(buildKey)

    Timber.v("Registering screens...")
    ScreenRegistry {
      register<HomeNavScreen> { HomeScreen() }
      register<ApodSingleNavScreen> { ApodSingleScreen(it.config) }
      register<ApodGridNavScreen> { ApodGridScreen(it.config) }
      register<ApodFullScreenNavScreen> { ApodFullScreen(it.item) }
      register<GallerySearchNavScreen> { GallerySearchScreen() }
      register<GalleryImageNavScreen> { GalleryImageScreen(it.id) }
      register<AboutNavScreen> { AboutScreen() }
      register<LicensesNavScreen> { LicensesScreen() }
      register<SettingsNavScreen> { SettingsScreen() }
    }

    Timber.v("Building ImageLoader...")
    Coil.setImageLoader(imageLoader)
    Timber.v("Done!")
  }
}
