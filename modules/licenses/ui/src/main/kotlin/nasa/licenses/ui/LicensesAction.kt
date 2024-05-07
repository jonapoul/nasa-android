package nasa.licenses.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface LicensesAction {
  data object NavBack : LicensesAction
  data object Reload : LicensesAction
  data class LaunchUrl(val url: String) : LicensesAction
}
