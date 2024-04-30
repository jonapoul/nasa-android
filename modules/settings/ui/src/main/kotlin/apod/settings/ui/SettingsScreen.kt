package apod.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SettingsScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.popAll()
      clickedBack = false
    }

    SettingsScreenImpl(
      onClickBack = { clickedBack = true },
    )
  }
}
