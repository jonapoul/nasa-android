package nasa.apod.nav

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
sealed interface ApodGridNavScreen

@Serializable
data object ApodGridTodayNavScreen : ApodGridNavScreen

@Serializable
data class ApodGridRandomNavScreen(val seed: Long = System.currentTimeMillis()) : ApodGridNavScreen

@Serializable
data class ApodGridSpecificNavScreen(val date: String) : ApodGridNavScreen {
  constructor(date: LocalDate) : this(date.toString())

  val localDate get() = LocalDate.parse(date)
}
