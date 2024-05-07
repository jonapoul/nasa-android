package nasa.settings.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface SettingsAction {
  data object NavBack : SettingsAction
  data object RegisterForKey : SettingsAction
  data object ClearCache : SettingsAction
}
