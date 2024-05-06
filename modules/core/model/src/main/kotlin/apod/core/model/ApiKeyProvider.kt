package apod.core.model

import kotlinx.coroutines.flow.Flow

// For fetching a key via DI
fun interface ApiKeyProvider {
  fun observe(): Flow<ApiKey?>
}
