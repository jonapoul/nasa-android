package apod.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class NavButtonsState(
  val enableNextButton: Boolean,
  val enablePrevButton: Boolean,
) {
  companion object {
    val BothDisabled = NavButtonsState(enableNextButton = false, enablePrevButton = false)
    val BothEnabled = NavButtonsState(enableNextButton = true, enablePrevButton = true)
  }
}
