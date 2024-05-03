package apod.settings.vm

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SettingsAction {
  data object NavBack : SettingsAction
  data object RegisterForKey : SettingsAction
}
