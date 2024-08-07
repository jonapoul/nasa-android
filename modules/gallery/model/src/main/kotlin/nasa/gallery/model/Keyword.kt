package nasa.gallery.model

@JvmInline
value class Keyword(val value: String) : CharSequence by value, Comparable<Keyword> {
  override fun toString(): String = value
  override fun compareTo(other: Keyword) = value.compareTo(other.value)
}
