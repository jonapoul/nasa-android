package nasa.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import nasa.about.nav.AboutNavScreen
import nasa.about.ui.AboutScreen
import nasa.apod.nav.ApodFullScreenNavScreen
import nasa.apod.nav.ApodGridRandomNavScreen
import nasa.apod.nav.ApodGridSpecificNavScreen
import nasa.apod.nav.ApodGridTodayNavScreen
import nasa.apod.nav.ApodScreenConfig
import nasa.apod.nav.ApodSingleRandomNavScreen
import nasa.apod.nav.ApodSingleSpecificNavScreen
import nasa.apod.nav.ApodSingleTodayNavScreen
import nasa.apod.ui.full.ApodFullScreen
import nasa.apod.ui.grid.ApodGridScreen
import nasa.apod.ui.single.ApodSingleScreen
import nasa.core.ui.NasaTheme
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

@AndroidEntryPoint
class NasaActivity : ComponentActivity() {
  private val viewModel by viewModels<NasaActivityViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()

    setContent {
      val theme by viewModel.theme.collectAsStateWithLifecycle()
      NasaTheme(theme) {
        NasaNavHost()
      }
    }
  }

  @Composable
  private fun NasaNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomeNavScreen) {
      composable<HomeNavScreen> { HomeScreen(navController) }

      apodScreens(navController)

      galleryScreens(navController)

      composable<AboutNavScreen> { AboutScreen(navController) }

      composable<LicensesNavScreen> { LicensesScreen(navController) }

      composable<SettingsNavScreen> { SettingsScreen(navController) }
    }
  }

  private fun NavGraphBuilder.apodScreens(navController: NavController) {
    composable<ApodSingleTodayNavScreen> {
      ApodSingleScreen(ApodScreenConfig.Today, navController)
    }

    composable<ApodSingleRandomNavScreen> { entry ->
      val args = entry.toRoute<ApodSingleRandomNavScreen>()
      ApodSingleScreen(ApodScreenConfig.Random(args.seed), navController)
    }

    composable<ApodSingleSpecificNavScreen> { entry ->
      val args = entry.toRoute<ApodSingleSpecificNavScreen>()
      ApodSingleScreen(ApodScreenConfig.Specific(args.localDate), navController)
    }

    composable<ApodGridTodayNavScreen> {
      ApodGridScreen(ApodScreenConfig.Today, navController)
    }

    composable<ApodGridRandomNavScreen> { entry ->
      val args = entry.toRoute<ApodGridRandomNavScreen>()
      ApodGridScreen(ApodScreenConfig.Random(args.seed), navController)
    }

    composable<ApodGridSpecificNavScreen> { entry ->
      val args = entry.toRoute<ApodGridSpecificNavScreen>()
      ApodGridScreen(ApodScreenConfig.Specific(args.localDate), navController)
    }

    composable<ApodFullScreenNavScreen> { entry ->
      val args = entry.toRoute<ApodFullScreenNavScreen>()
      ApodFullScreen(args.localDate, navController)
    }
  }

  private fun NavGraphBuilder.galleryScreens(navController: NavController) {
    composable<GallerySearchNavScreen> { GallerySearchScreen(navController) }

    composable<GalleryImageNavScreen> { entry ->
      val args = entry.toRoute<GalleryImageNavScreen>()
      GalleryImageScreen(args.nasaId, navController)
    }
  }
}
