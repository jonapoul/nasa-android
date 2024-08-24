package nasa.about.ui

import alakazam.kotlin.core.noOp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.getViewModel
import nasa.core.ui.set
import nasa.licenses.nav.LicensesNavScreen

class AboutScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val theme = LocalTheme.current

    val viewModel = getViewModel<AboutViewModel>()
    val buildState by viewModel.buildState.collectAsStateWithLifecycle()

    val clickedBack = remember { mutableStateOf(false) }
    if (clickedBack.value) {
      navigator.popAll()
      clickedBack.set(false)
    }

    val licensesScreen = rememberScreen(LicensesNavScreen)
    val clickedLicenses = remember { mutableStateOf(false) }
    if (clickedLicenses.value) {
      navigator.push(licensesScreen)
      clickedLicenses.set(false)
    }

    val checkUpdates = remember { mutableStateOf(false) }
    val onCancel = {
      checkUpdates.set(false)
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

    if (checkUpdates.value) {
      LaunchedEffect(Unit) { viewModel.fetchLatestRelease() }
    }

    AboutScreenImpl(
      buildState = buildState,
      onAction = { action ->
        when (action) {
          AboutAction.OpenSourceCode -> viewModel.openRepo()
          AboutAction.ReportIssue -> viewModel.reportIssues()
          AboutAction.CheckUpdates -> checkUpdates.set(true)
          AboutAction.NavBack -> clickedBack.set(true)
          AboutAction.ViewLicenses -> clickedLicenses.set(true)
        }
      },
    )
  }
}
