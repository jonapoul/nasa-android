@file:Suppress("DataClassPrivateConstructor")

package nasa.apod.nav

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ApodFullScreenNavScreen(val date: String) {
  constructor(date: LocalDate) : this(date.toString())

  val localDate: LocalDate get() = LocalDate.parse(date)
}
