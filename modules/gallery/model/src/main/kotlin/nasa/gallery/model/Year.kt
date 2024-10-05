package nasa.gallery.model

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@JvmInline
value class Year(val value: Int) : Comparable<Year> {
  constructor(value: Number) : this(value.toInt())

  init {
    require(value > 0) { "Year must be positive, got $value" }
  }

  override fun compareTo(other: Year): Int = value.compareTo(other.value)
  override fun toString(): String = "%04d".format(value)

  fun toInt() = value
  fun toFloat() = value.toFloat()

  companion object {
    val Minimum = Year(value = 1900)

    val Maximum = Clock.System
      .now()
      .toLocalDateTime(TimeZone.currentSystemDefault())
      .year
      .let(::Year)
  }
}
