package apod.single.ui

import androidx.compose.runtime.Stable
import apod.core.model.ApiKey
import apod.single.vm.ScreenState
import kotlinx.datetime.LocalDate

@Stable
internal fun ScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = dateOrNull() ?: return
  block(date)
}

@Stable
internal fun ScreenState.dateOrNull(): LocalDate? = when (this) {
  ScreenState.Inactive, is ScreenState.NoApiKey -> null
  is ScreenState.Failed -> date
  is ScreenState.Loading -> date
  is ScreenState.Success -> item.date
}

@Stable
internal fun ScreenState.apiKeyOrNull(): ApiKey? = when (this) {
  ScreenState.Inactive -> null
  is ScreenState.NoApiKey -> null
  is ScreenState.Failed -> key
  is ScreenState.Loading -> key
  is ScreenState.Success -> key
}
