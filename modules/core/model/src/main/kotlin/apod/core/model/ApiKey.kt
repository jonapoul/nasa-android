package apod.core.model

import androidx.compose.runtime.Immutable

@Immutable
@JvmInline
value class ApiKey(private val value: String) {
  override fun toString(): String = value
}
