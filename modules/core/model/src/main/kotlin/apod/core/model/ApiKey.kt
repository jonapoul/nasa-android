package apod.core.model

@JvmInline
value class ApiKey(private val value: String) {
  override fun toString(): String = value
}
