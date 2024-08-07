package nasa.gallery.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Center(val value: String) : CharSequence by value, Comparable<Center> {
  override fun toString(): String = value
  override fun compareTo(other: Center) = value.compareTo(other.value)
}
