package apod.licenses.vm

import androidx.compose.runtime.Immutable

@Immutable
sealed interface LicensesAction {
  data object NavBack : LicensesAction
  data object Reload : LicensesAction
  data class LaunchUrl(val url: String) : LicensesAction
}
