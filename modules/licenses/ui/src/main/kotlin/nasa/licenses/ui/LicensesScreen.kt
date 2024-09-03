package nasa.licenses.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import nasa.core.ui.set
import nasa.licenses.vm.LicensesViewModel

@Composable
fun LicensesScreen(
  navController: NavController,
  viewModel: LicensesViewModel = hiltViewModel<LicensesViewModel>(),
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  val clickedBack = remember { mutableStateOf(false) }
  if (clickedBack.value) {
    navController.popBackStack()
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
