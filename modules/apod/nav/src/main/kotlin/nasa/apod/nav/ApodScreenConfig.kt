package nasa.apod.nav

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface ApodScreenConfig {
  data object Today : ApodScreenConfig

  // Using a psuedo-random seed here helps ensure that we can properly reload the screen when we hit the random
  // button multiple times in a row. If we make this a data object instead, the second randomise won't work!
  // The seed value is only used to ensure equals() and hashCode() return pseudo-unique values every time we create
  // a new screen - it's not referenced anywhere. There's probably a better way of doing this...
  @Serializable
  data class Random(
    val seed: Long = System.currentTimeMillis(),
  ) : ApodScreenConfig

  @Immutable
  @Serializable
  data class Specific(
    val date: LocalDate,
  ) : ApodScreenConfig
}
