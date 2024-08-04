package nasa.gallery.model

@JvmInline
value class Keyword(val value: String) : CharSequence by value {
  override fun toString(): String = value
}
