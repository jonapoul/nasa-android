package nasa.gallery.model

@JvmInline
value class Keywords private constructor(private val value: String) {
  constructor(vararg keywords: String) : this(keywords.joinToString(SEPARATOR))
  constructor(keywords: Iterable<Keyword>) : this(keywords.joinToString(SEPARATOR))

  @Deprecated(message = "Unsupported constructor", level = DeprecationLevel.ERROR)
  constructor() : this(keywords = listOf())

  override fun toString(): String = value

  fun toList(): List<Keyword> = value.split(SEPARATOR).map(::Keyword)

  companion object {
    const val SEPARATOR = ","

    fun from(vararg strings: String): Keywords = from(strings.toList())

    fun from(strings: Iterable<String>): Keywords = Keywords(strings.map(::Keyword))
  }
}
