package nasa.settings.ui

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
import nasa.settings.vm.SettingsViewModel

class SettingsScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<SettingsViewModel>()
    val cacheSize by viewModel.cacheSize.collectAsStateWithLifecycle()
    val databaseSize by viewModel.databaseSize.collectAsStateWithLifecycle()

    val clickedBack = remember { mutableStateOf(false) }
    if (clickedBack.value) {
      navigator.popAll()
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
}
