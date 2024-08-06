package nasa.gallery.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Album(val value: String) : CharSequence by value {
  override fun toString(): String = value
}
