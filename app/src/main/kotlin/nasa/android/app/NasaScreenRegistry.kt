package nasa.android.app

import cafe.adriel.voyager.core.registry.ScreenRegistry
import nasa.about.nav.AboutNavScreen
import nasa.about.ui.AboutScreen
import nasa.apod.nav.ApodFullScreenNavScreen
import nasa.apod.nav.ApodGridNavScreen
import nasa.apod.nav.ApodSingleNavScreen
import nasa.apod.ui.grid.ApodGridScreen
import nasa.apod.ui.single.ApodFullScreen
import nasa.apod.ui.single.ApodSingleScreen
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

object NasaScreenRegistry {
  operator fun invoke() {
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
  }
}
