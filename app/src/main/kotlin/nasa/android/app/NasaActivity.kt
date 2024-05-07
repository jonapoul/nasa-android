package nasa.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import nasa.apod.single.ui.ApodSingleScreen
import nasa.core.ui.NasaTheme
import nasa.nav.ApodScreenConfig

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
        Navigator(ApodSingleScreen(ApodScreenConfig.Today))
      }
    }
  }
}
