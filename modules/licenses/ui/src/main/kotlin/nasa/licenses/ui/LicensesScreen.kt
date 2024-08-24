package nasa.licenses.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.getViewModel
import nasa.core.ui.set

class LicensesScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<LicensesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val clickedBack = remember { mutableStateOf(false) }
    if (clickedBack.value) {
      navigator.pop()
      clickedBack.set(false)
    }

    LicensesScreenImpl(
      state = state,
      onAction = { action ->
        when (action) {
          LicensesAction.NavBack -> clickedBack.set(true)
          LicensesAction.Reload -> viewModel.load()
          is LicensesAction.LaunchUrl -> viewModel.openUrl(action.url)
        }
      },
    )
  }
}
