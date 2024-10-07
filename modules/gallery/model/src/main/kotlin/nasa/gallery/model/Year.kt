package nasa.gallery.model

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

@JvmInline
value class Year(val value: Int) : Comparable<Year> {
  constructor(value: Number) : this(
    when (value) {
      is Float -> value.roundToInt()
      is Double -> value.roundToInt()
      else -> value.toInt()
    }
  )

  init {
    require(value > 0) { "Year must be positive, got $value" }
  }

  override fun compareTo(other: Year): Int = value.compareTo(other.value)
  override fun toString(): String = "%04d".format(value)

  fun toInt() = value
  fun toFloat() = value.toFloat()

  companion object {
    val Minimum = 1900.year

    val Maximum = Clock.System
      .now()
      .toLocalDateTime(TimeZone.currentSystemDefault())
      .year
      .year
  }
}

val Number.year: Year get() = Year(value = this)
