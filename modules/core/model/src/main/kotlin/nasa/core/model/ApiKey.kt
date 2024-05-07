package nasa.core.model

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

@Immutable
@JvmInline
value class ApiKey(private val value: String) {
  override fun toString(): String = value

  // For fetching a key via DI
  fun interface Provider {
    fun observe(): Flow<ApiKey?>
  }
}
