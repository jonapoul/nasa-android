package apod.grid.ui

import androidx.compose.runtime.Stable
import apod.grid.vm.GridScreenState
import apod.grid.vm.dateOrNull
import kotlinx.datetime.LocalDate

@Stable
internal fun GridScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = dateOrNull() ?: return
  block(date)
}
