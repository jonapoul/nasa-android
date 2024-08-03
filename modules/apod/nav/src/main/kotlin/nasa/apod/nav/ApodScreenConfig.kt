package nasa.apod.nav

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Needs to be [Serializable] so we can pass it as a screen parameter with Voyager.
 */
@Immutable
sealed interface ApodScreenConfig : Serializable {
  data object Today : ApodScreenConfig {
    private fun readResolve(): Any = Today
  }

  // Using a psuedo-random seed here helps ensure that we can properly reload the screen when we hit the random
  // button multiple times in a row. If we make this a data object instead, the second randomise won't work!
  // The seed value is only used to ensure equals() and hashCode() return pseudo-unique values every time we create
  // a new screen - it's not referenced anywhere. There's probably a better way of doing this...
  data class Random(
    private val seed: Long = System.currentTimeMillis(),
  ) : ApodScreenConfig

  // Needs custom serialization of [LocalDate] because it's not [Serializable]. Annoyingly I can't get serialization
  // to work without making [date] a var - but we're using [Immutable] to pinky-promise to the compose compiler that
  // we won't change it at runtime.
  @Immutable
  data class Specific(var date: LocalDate) : ApodScreenConfig {
    @Throws(IOException::class)
    private fun writeObject(output: ObjectOutputStream) {
      output.writeObject(date.toString())
    }

    @Throws(IOException::class)
    private fun readObject(input: ObjectInputStream) {
      date = LocalDate.parse(input.readObject() as String)
    }
  }
}
