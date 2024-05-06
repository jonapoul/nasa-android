package apod.licenses.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.core.ui.getViewModel
import apod.licenses.vm.LicensesViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class LicensesScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<LicensesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.pop()
      clickedBack = false
    }

    LicensesScreenImpl(
      state = state,
      onAction = {
        when (it) {
          LicensesAction.NavBack -> {
            clickedBack = true
          }

          LicensesAction.Reload -> {
            viewModel.load()
          }

          is LicensesAction.LaunchUrl -> {
            viewModel.openUrl(it.url)
          }
        }
      },
    )
  }
}
