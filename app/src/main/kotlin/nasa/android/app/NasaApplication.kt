package nasa.android.app

import alakazam.android.core.IBuildConfig
import android.app.Application
import coil.Coil
import dagger.hilt.android.HiltAndroidApp
import nasa.android.BuildConfig
import nasa.core.di.PreferencesApiKeyProvider
import nasa.core.model.ApiKey
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

    Timber.d("Setting API key...")
    // API_KEY can be null, based on gradle script logic
    @Suppress("UNNECESSARY_SAFE_CALL")
    val buildKey = BuildConfig.API_KEY?.let(::ApiKey)
    apiKeyProvider.set(buildKey)

    Timber.d("Building ImageLoader...")
    Coil.setImageLoader(imageLoader)
    Timber.d("Done!")
  }
}
