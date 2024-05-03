package apod.single.ui

import androidx.compose.runtime.Stable
import apod.core.model.ApiKey
import apod.single.vm.ScreenState
import kotlinx.datetime.LocalDate

@Stable
internal fun ScreenState.ifHasDateAndKey(block: (LocalDate, ApiKey) -> Unit) {
  when (this) {
    ScreenState.Inactive -> return
    is ScreenState.NoApiKey -> return
    is ScreenState.Failed -> block(date ?: return, key)
    is ScreenState.Loading -> block(date ?: return, key)
    is ScreenState.Success -> block(item.date, key)
  }
}
