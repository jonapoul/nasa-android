package apod.nav

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
sealed interface ScreenConfig : Serializable {
  data object Today : ScreenConfig {
    private fun readResolve(): Any = Today
  }

  data object Random : ScreenConfig {
    private fun readResolve(): Any = Random
  }

  /**
   * Needs custom serialization of [LocalDate] because it's not [Serializable].
   * Annoyingly I can't get serialization to work without making [date] a var - but I pinky-promise to the compose
   * compiler that I won't change it at runtime.
   */
  @Immutable
  data class Specific(var date: LocalDate) : ScreenConfig {
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
