package apod.grid.ui

import androidx.compose.runtime.Stable
import apod.grid.vm.GridScreenState
import kotlinx.datetime.LocalDate

@Stable
internal fun GridScreenState.dateOrNull(): LocalDate? = when (this) {
  GridScreenState.Inactive, GridScreenState.NoApiKey -> null
  is GridScreenState.Failed -> date
  is GridScreenState.Loading -> date
  is GridScreenState.Success -> items.firstOrNull()?.date
}

@Stable
internal fun GridScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = dateOrNull() ?: return
  block(date)
}
