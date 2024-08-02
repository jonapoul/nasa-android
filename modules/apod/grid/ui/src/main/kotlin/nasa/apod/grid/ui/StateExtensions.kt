package nasa.apod.grid.ui

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import nasa.apod.grid.vm.GridScreenState
import nasa.apod.grid.vm.dateOrNull

@Stable
internal fun GridScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = dateOrNull() ?: return
  block(date)
}
