package nasa.core.model.test

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nasa.core.model.ApiKey

class InMemoryApiKeyProvider(initial: ApiKey? = null) : ApiKey.Provider {
  private val mutableState = MutableStateFlow(initial)

  fun set(key: ApiKey?) = mutableState.update { key }

  override fun observe() = mutableState.asStateFlow()
}
