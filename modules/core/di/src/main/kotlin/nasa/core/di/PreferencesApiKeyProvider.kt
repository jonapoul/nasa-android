package nasa.core.di

import dev.jonpoulton.preferences.core.Preferences
import dev.jonpoulton.preferences.core.SimpleNullableStringSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import nasa.core.model.ApiKey
import nasa.core.model.SettingsKeys
import javax.inject.Inject

class PreferencesApiKeyProvider @Inject constructor(preferences: Preferences) : ApiKey.Provider {
  private val apiKeySerializer = SimpleNullableStringSerializer { value -> value?.let(::ApiKey) }

  private val preference = preferences.getNullableObject(SettingsKeys.API_KEY, apiKeySerializer, default = null)

  fun set(key: ApiKey?) {
    val existing = preference.get()
    if (existing == null && key != null) {
      preference.set(key)
    }
  }

  override fun observe(): Flow<ApiKey?> = preference.asFlow().distinctUntilChanged()
}
