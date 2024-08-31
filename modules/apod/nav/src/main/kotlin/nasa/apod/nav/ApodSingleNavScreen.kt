package nasa.apod.nav

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
sealed interface ApodSingleNavScreen

@Serializable
data object ApodSingleTodayNavScreen : ApodSingleNavScreen

@Serializable
data class ApodSingleRandomNavScreen(val seed: Long = System.currentTimeMillis()) : ApodSingleNavScreen

@Serializable
data class ApodSingleSpecificNavScreen(val date: String) : ApodSingleNavScreen {
  constructor(date: LocalDate) : this(date.toString())

  val localDate get() = LocalDate.parse(date)
}
