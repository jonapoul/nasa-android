package apod.core.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

// Needs to be serializable so we can pass it as a screen parameter with Voyager.
@Immutable
sealed interface GridScreenConfig {
  @Serializable
  data object ThisMonth : GridScreenConfig

  @Serializable
  data object Random : GridScreenConfig

  @Serializable
  data class Specific(val date: LocalDate) : GridScreenConfig
}
