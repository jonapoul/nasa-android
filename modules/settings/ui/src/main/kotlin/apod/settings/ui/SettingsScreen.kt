package apod.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.core.ui.getViewModel
import apod.settings.vm.SettingsAction
import apod.settings.vm.SettingsViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SettingsScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<SettingsViewModel>()
    val cacheSize by viewModel.cacheSize.collectAsStateWithLifecycle()
    val databaseSize by viewModel.databaseSize.collectAsStateWithLifecycle()

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.popAll()
      clickedBack = false
    }

    var showClearCacheDialog by remember { mutableStateOf(false) }
    val immutableShowDialog = showClearCacheDialog
    if (immutableShowDialog) {
      ConfirmClearCacheDialog(
        totalSize = cacheSize + databaseSize,
        onConfirm = {
          viewModel.clearCache()
          showClearCacheDialog = false
        },
        onCancel = { showClearCacheDialog = false },
      )
    }

    SettingsScreenImpl(
      imagesSize = cacheSize,
      databaseSize = databaseSize,
      onAction = { action ->
        when (action) {
          SettingsAction.NavBack -> {
            clickedBack = true
          }

          SettingsAction.ClearCache -> {
            showClearCacheDialog = true
          }

          SettingsAction.RegisterForKey -> {
            viewModel.registerForApiKey()
          }
        }
      },
    )
  }
}
