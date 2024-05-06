package apod.single.ui

import androidx.compose.runtime.Stable
import apod.core.model.ApiKey
import apod.single.vm.ScreenState
import apod.single.vm.dateOrNull
import kotlinx.datetime.LocalDate

@Stable
internal fun ScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = dateOrNull() ?: return
  block(date)
}

@Stable
internal fun ScreenState.apiKeyOrNull(): ApiKey? = when (this) {
  ScreenState.Inactive -> null
  is ScreenState.NoApiKey -> null
  is ScreenState.Failed -> key
  is ScreenState.Loading -> key
  is ScreenState.Success -> key
}
