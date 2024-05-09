package nasa.gallery.model

@JvmInline
value class MediaTypes private constructor(private val value: String) {
  constructor(vararg types: MediaType) : this(types.joinToString(SEPARATOR))
  constructor(types: Iterable<MediaType>) : this(types.joinToString(SEPARATOR))

  @Deprecated(message = "Unsupported constructor", level = DeprecationLevel.ERROR)
  constructor() : this(types = listOf())

  override fun toString(): String = value
  fun toList(): List<MediaType> = value.split(SEPARATOR).map(MediaType::parse)

  companion object {
    const val SEPARATOR = ","
  }
}
