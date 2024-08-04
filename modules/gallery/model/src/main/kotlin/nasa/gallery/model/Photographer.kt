package nasa.gallery.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Photographer(val value: String) : CharSequence by value {
  override fun toString(): String = value
}
