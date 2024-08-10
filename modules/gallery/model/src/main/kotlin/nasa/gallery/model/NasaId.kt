package nasa.gallery.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class NasaId(private val value: String) : CharSequence by value, Comparable<NasaId> {
  override fun toString(): String = value
  override fun compareTo(other: NasaId) = value.compareTo(other.value)
}
