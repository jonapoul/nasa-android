package nasa.gallery.model

import kotlinx.serialization.SerialName

@JvmInline
value class MediaTypes private constructor(private val value: String) {
  constructor(vararg types: MediaType) : this(types.joinToString(SEPARATOR) { it.serialName() })
  constructor(types: Iterable<MediaType>) : this(types.joinToString(SEPARATOR) { it.serialName() })

  @Deprecated(message = "Unsupported constructor", level = DeprecationLevel.ERROR)
  constructor() : this(types = listOf())

  override fun toString(): String = value

  fun toSet(): Set<MediaType> {
    if (value.isEmpty()) return emptySet()
    return value
      .split(SEPARATOR)
      .map { name -> NAME_MAP[name] ?: error("Invalid MediaType '$name'") }
      .toSet()
  }

  companion object {
    val All = MediaTypes(MediaType.entries)
    val Empty = MediaTypes(emptySet())

    private const val SEPARATOR = ","

    private val NAME_MAP: Map<String, MediaType> = MediaType
      .entries
      .associateBy { mediaType -> mediaType.serialName() }

    private fun MediaType.serialName() = MediaType::class.java
      .getDeclaredField(name)
      .getAnnotation(SerialName::class.java)
      ?.value
      ?: error("No SerialName for $this")
  }
}
