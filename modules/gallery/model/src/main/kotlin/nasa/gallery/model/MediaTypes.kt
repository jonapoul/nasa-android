package nasa.gallery.model

import kotlinx.serialization.SerialName

@JvmInline
value class MediaTypes private constructor(private val value: String) {
  constructor(vararg types: MediaType) : this(types.joinToString(SEPARATOR) { it.serialName() })
  constructor(types: Iterable<MediaType>) : this(types.joinToString(SEPARATOR) { it.serialName() })

  @Deprecated(message = "Unsupported constructor", level = DeprecationLevel.ERROR)
  constructor() : this(types = listOf())

  override fun toString(): String = value

  companion object {
    const val SEPARATOR = ","

    private fun MediaType.serialName() = MediaType::class.java
      .getDeclaredField(name)
      .getAnnotation(SerialName::class.java)
      .also { if (it == null) error("No SerialName for $this") }
      .value
  }
}
