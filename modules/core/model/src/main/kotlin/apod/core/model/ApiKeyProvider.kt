package apod.core.model

// For fetching a key via DI
fun interface ApiKeyProvider {
  fun get(): ApiKey
}
