package apod.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import apod.settings.vm.SettingsAction
import apod.settings.vm.SettingsViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SettingsScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<SettingsViewModel>()

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.popAll()
      clickedBack = false
    }

    SettingsScreenImpl(
      onAction = {
        when (it) {
          SettingsAction.NavBack -> {
            clickedBack = true
          }

          SettingsAction.RegisterForKey -> {
            viewModel.registerForApiKey()
          }
        }
      },
    )
  }
}
