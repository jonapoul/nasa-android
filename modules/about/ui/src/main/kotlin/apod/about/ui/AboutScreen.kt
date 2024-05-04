package apod.about.ui

import alakazam.kotlin.core.noOp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.about.vm.AboutAction
import apod.about.vm.AboutViewModel
import apod.about.vm.CheckUpdatesState
import apod.core.ui.color.LocalTheme
import apod.core.ui.getViewModel
import apod.navigation.NavScreens
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class AboutScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current

    val viewModel = getViewModel<AboutViewModel>()
    val buildState by viewModel.buildState.collectAsStateWithLifecycle()

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.popAll()
      clickedBack = false
    }

    val licensesScreen = rememberScreen(NavScreens.Licenses)
    var clickedLicenses by remember { mutableStateOf(false) }
    if (clickedLicenses) {
      navigator.push(licensesScreen)
      clickedLicenses = false
    }

    var checkUpdates by remember { mutableStateOf(false) }
    val onCancel = {
      checkUpdates = false
      viewModel.cancelUpdateCheck()
    }

    val checkUpdatesState by viewModel.checkUpdatesState.collectAsStateWithLifecycle()
    when (val state = checkUpdatesState) {
      CheckUpdatesState.Inactive -> noOp()
      CheckUpdatesState.Checking -> CheckUpdatesLoadingDialog(onCancel, theme = theme)
      CheckUpdatesState.NoUpdateFound -> NoUpdateFoundDialog(onCancel, theme = theme)
      is CheckUpdatesState.Failed -> UpdateCheckFailedDialog(state.cause, onCancel, theme = theme)

      is CheckUpdatesState.UpdateFound -> UpdateFoundDialog(
        currentVersion = buildState.buildVersion,
        latestVersion = state.version,
        latestUrl = state.url,
        onDismiss = onCancel,
        onOpenUrl = { viewModel.openUrl(it) },
        theme = theme,
      )
    }

    if (checkUpdates) {
      LaunchedEffect(Unit) { viewModel.fetchLatestRelease() }
    }

    AboutScreenImpl(
      buildState = buildState,
      onAction = {
        when (it) {
          AboutAction.OpenSourceCode -> {
            viewModel.openRepo()
          }

          AboutAction.ReportIssue -> {
            viewModel.reportIssues()
          }

          AboutAction.CheckUpdates -> {
            checkUpdates = true
          }

          AboutAction.NavBack -> {
            clickedBack = true
          }

          AboutAction.ViewLicenses -> {
            clickedLicenses = true
          }
        }
      },
    )
  }
}
