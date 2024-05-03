package apod.single.ui

import androidx.compose.runtime.Stable
import apod.single.vm.ScreenState
import kotlinx.datetime.LocalDate

@Stable
internal fun ScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = when (this) {
    ScreenState.Inactive -> null
    is ScreenState.Failed -> date
    is ScreenState.Loading -> date
    is ScreenState.Success -> item.date
  } ?: return
  block(date)
}
