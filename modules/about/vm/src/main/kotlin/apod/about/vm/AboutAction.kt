package apod.about.vm

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AboutAction {
  data object NavBack : AboutAction
  data object ViewLicenses : AboutAction
  data object CheckUpdates : AboutAction
  data object ReportIssue : AboutAction
  data object OpenSourceCode : AboutAction
}
