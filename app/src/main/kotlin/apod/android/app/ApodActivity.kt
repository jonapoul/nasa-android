package apod.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.core.model.ApodLoadType
import apod.core.ui.ApodTheme
import apod.single.ui.ApodSingleScreen
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApodActivity : ComponentActivity() {
  private val viewModel by viewModels<ApodActivityViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()

    setContent {
      val theme by viewModel.theme.collectAsStateWithLifecycle()
      ApodTheme(theme) {
        Navigator(ApodSingleScreen(ApodLoadType.Today))
      }
    }
  }
}
