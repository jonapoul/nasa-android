package nasa.core.model

import kotlinx.coroutines.flow.Flow

@JvmInline
value class ApiKey(private val value: String) {
  override fun toString(): String = value

  // For fetching a key via DI
  fun interface Provider {
    fun observe(): Flow<ApiKey?>
  }

  companion object {
    val DEMO = ApiKey(value = "DEMO_KEY")
  }
}
