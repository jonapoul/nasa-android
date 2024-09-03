package nasa.apod.ui.single

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import nasa.apod.vm.single.ScreenState
import nasa.apod.vm.single.dateOrNull
import nasa.core.model.ApiKey

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
