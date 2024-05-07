package nasa.apod.model

import androidx.compose.runtime.Immutable

@Immutable
data class ApodNavButtonsState(
  val enableNext: Boolean,
  val enablePrevious: Boolean,
) {
  companion object {
    val BothDisabled = ApodNavButtonsState(enableNext = false, enablePrevious = false)
    val BothEnabled = ApodNavButtonsState(enableNext = true, enablePrevious = true)
  }
}
