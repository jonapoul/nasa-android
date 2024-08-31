package nasa.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import nasa.core.ui.set

@Composable
fun SettingsScreen(
  navController: NavController,
  viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
) {
  val cacheSize by viewModel.cacheSize.collectAsStateWithLifecycle()
  val databaseSize by viewModel.databaseSize.collectAsStateWithLifecycle()

  val clickedBack = remember { mutableStateOf(false) }
  if (clickedBack.value) {
    navController.popBackStack()
    clickedBack.set(false)
  }

  val showClearCacheDialog = remember { mutableStateOf(false) }
  val immutableShowDialog = showClearCacheDialog.value
  if (immutableShowDialog) {
    ConfirmClearCacheDialog(
      totalSize = cacheSize + databaseSize,
      onConfirm = {
        viewModel.clearCache()
        showClearCacheDialog.set(false)
      },
      onCancel = { showClearCacheDialog.set(false) },
    )
  }

  SettingsScreenImpl(
    imagesSize = cacheSize,
    databaseSize = databaseSize,
    onAction = { action ->
      when (action) {
        SettingsAction.NavBack -> clickedBack.set(true)
        SettingsAction.ClearCache -> showClearCacheDialog.set(true)
        SettingsAction.RegisterForKey -> viewModel.registerForApiKey()
      }
    },
  )
}
