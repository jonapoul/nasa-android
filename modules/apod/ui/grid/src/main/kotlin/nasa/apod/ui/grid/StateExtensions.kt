package nasa.apod.ui.grid

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate

@Stable
internal fun GridScreenState.ifHasDate(block: (LocalDate) -> Unit) {
  val date = dateOrNull() ?: return
  block(date)
}
