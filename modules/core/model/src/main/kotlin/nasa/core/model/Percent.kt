@file:Suppress("MagicNumber")

package nasa.core.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.math.roundToInt

@Immutable
@JvmInline
value class Percent(val value: Int) : Comparable<Percent> {
  constructor(value: Number) : this(value.toFloat().roundToInt())

  constructor(numerator: Number, denominator: Number) : this(
    value = 100.0 * numerator.toDouble() / denominator.toDouble(),
  )

  override fun compareTo(other: Percent): Int = value.compareTo(other.value)

  override fun toString(): String = "$value%"

  @Stable
  fun toFraction(): Float = value.toFloat() / 100f

  companion object {
    val Zero = 0.percent
    val OneHundred = 100.percent
  }
}

val Number.percent: Percent get() = Percent(value = this)