package apod.android.app

import alakazam.android.core.IBuildConfig
import android.app.Application
import apod.about.ui.AboutScreen
import apod.licenses.ui.LicensesScreen
import apod.navigation.NavScreens
import apod.settings.ui.SettingsScreen
import apod.single.ui.ApodSingleScreen
import cafe.adriel.voyager.core.registry.ScreenRegistry
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ApodApplication : Application() {
  @Inject
  lateinit var bc: IBuildConfig

  override fun onCreate() {
    super.onCreate()
    Timber.plant(ApodTree())

    Timber.i("onCreate")
    Timber.d("name=${bc.versionName} code=${bc.versionCode} time=${bc.buildTime}")
    Timber.d("manufacturer=${bc.manufacturer} model=${bc.model} os=${bc.os} platform=${bc.platform}")

    ScreenRegistry {
      register<NavScreens.Home> { ApodSingleScreen() }
      register<NavScreens.About> { AboutScreen() }
      register<NavScreens.Licenses> { LicensesScreen() }
      register<NavScreens.Settings> { SettingsScreen() }
    }
  }
}
