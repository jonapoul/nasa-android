package apod.core.model

import kotlinx.datetime.LocalDate

fun interface Calendar {
  fun today(): LocalDate
}
