package apod.android.app

import alakazam.android.prefs.core.getNullableObject
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import apod.core.model.SettingsKeys
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.NullableSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import javax.inject.Inject

class ApiKeyManager @Inject constructor(flowPrefs: FlowSharedPreferences) : ApiKeyProvider {
  private val preference = flowPrefs.getNullableObject(SettingsKeys.ApiKey, ApiKeySerializer)

  private object ApiKeySerializer : NullableSerializer<ApiKey> {
    override fun deserialize(serialized: String?): ApiKey? = serialized?.let(::ApiKey)
    override fun serialize(value: ApiKey?): String? = value?.toString()
  }

  fun set(key: ApiKey?) {
    val existing = preference.get()
    Timber.v("set $key, existing = $existing")
    if (existing == null && key != null) {
      preference.set(key)
    }
  }

  override fun observe(): Flow<ApiKey?> = preference.asFlow().distinctUntilChanged()
}
