package nasa.apod.model

data class ApodNavButtonsState(
  val enableNext: Boolean,
  val enablePrevious: Boolean,
) {
  companion object {
    val BothDisabled = ApodNavButtonsState(enableNext = false, enablePrevious = false)
    val BothEnabled = ApodNavButtonsState(enableNext = true, enablePrevious = true)
  }
}
