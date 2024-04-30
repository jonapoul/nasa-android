package apod.single.ui

import androidx.compose.runtime.Stable
import apod.single.vm.LoadState
import kotlinx.datetime.LocalDate

@Stable
internal fun LoadState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = when (this) {
    LoadState.Inactive -> return
    is LoadState.Failed -> date
    is LoadState.Loading -> date
    is LoadState.Success -> item.date
  }
  block(date)
}
