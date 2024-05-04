package apod.core.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

// Needs to be serializable so we can pass it as a screen parameter with Voyager.
@Immutable
sealed interface SingleScreenConfig {
  @Serializable
  data object Today : SingleScreenConfig

  @Serializable
  data object Random : SingleScreenConfig

  @Serializable
  data class Specific(val date: LocalDate) : SingleScreenConfig
}
