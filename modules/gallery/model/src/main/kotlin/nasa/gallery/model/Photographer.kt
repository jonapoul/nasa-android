package nasa.gallery.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Photographer(val value: String) : CharSequence by value, Comparable<Photographer> {
  override fun toString(): String = value
  override fun compareTo(other: Photographer) = value.compareTo(other.value)
}
