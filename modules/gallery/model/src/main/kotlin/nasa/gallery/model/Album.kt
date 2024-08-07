package nasa.gallery.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Album(val value: String) : CharSequence by value, Comparable<Album> {
  override fun toString(): String = value
  override fun compareTo(other: Album) = value.compareTo(other.value)
}
