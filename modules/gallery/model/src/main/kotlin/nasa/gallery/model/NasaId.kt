package nasa.gallery.model

@JvmInline
value class NasaId(private val value: String) : CharSequence by value {
  override fun toString(): String = value
}
