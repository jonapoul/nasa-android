package nasa.gallery.model

@JvmInline
value class Year(val value: Int) : Comparable<Year> {
  init {
    require(value > 0) { "Year must be positive, got $value" }
  }

  override fun compareTo(other: Year): Int = value.compareTo(other.value)
  override fun toString(): String = "%04d".format(value)
}
