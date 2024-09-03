package nasa.about.ui

import alakazam.kotlin.core.noOp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import nasa.about.vm.AboutViewModel
import nasa.about.vm.CheckUpdatesState
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.set
import nasa.licenses.nav.LicensesNavScreen

@Composable
fun AboutScreen(
  navController: NavController,
  viewModel: AboutViewModel = hiltViewModel(),
) {
  val theme = LocalTheme.current
  val buildState by viewModel.buildState.collectAsStateWithLifecycle()

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
        AboutAction.NavBack -> navController.popBackStack()
        AboutAction.ViewLicenses -> navController.navigate(LicensesNavScreen)
      }
    },
  )
}
