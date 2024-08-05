package nasa.android.di

import alakazam.kotlin.core.StateHolder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.combine
import nasa.android.app.ApiKeyManager
import nasa.core.http.ApiUsage
import nasa.core.http.ApiUsageDataSource
import nasa.core.http.ApiUsageDataStore
import nasa.core.http.ApiUsageState
import nasa.core.model.ApiKey
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ApiModule {
  @Binds
  @Singleton
  fun apiKey(impl: ApiKeyManager): ApiKey.Provider

  @Binds
  @Singleton
  fun usageStore(holder: ApiUsageStateHolder): ApiUsageDataStore

  @Binds
  fun usageSource(source: ApiUsageDataSourceImpl): ApiUsageDataSource
}

@Singleton
internal class ApiUsageStateHolder @Inject constructor() : StateHolder<ApiUsage?>(null), ApiUsageDataStore {
  override fun store(usage: ApiUsage) = set(usage)
}

internal class ApiUsageDataSourceImpl @Inject constructor(
  private val holder: ApiUsageStateHolder,
  private val provider: ApiKey.Provider,
) : ApiUsageDataSource {
  override fun observe() = combine(holder.state, provider.observe(), ::toState)

  private fun toState(usage: ApiUsage?, key: ApiKey?): ApiUsageState = when {
    key == null -> {
      ApiUsageState.NoApiKey
    }

    key == ApiKey.DEMO -> if (usage == null) {
      ApiUsageState.DemoKeyNoUsage
    } else {
      ApiUsageState.DemoKeyHasUsage(usage.remaining, usage.upperLimit)
    }

    usage == null -> ApiUsageState.RealKeyNoUsage

    else -> ApiUsageState.RealKeyHasUsage(usage.remaining, usage.upperLimit)
  }
}
