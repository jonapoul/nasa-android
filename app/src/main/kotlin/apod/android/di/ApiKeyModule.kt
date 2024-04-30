package apod.android.di

import apod.android.BuildConfig
import apod.core.model.ApiKey
import apod.core.model.ApiKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiKeyModule {
  @Provides
  @Singleton
  fun apiKey(): ApiKeyProvider = DefaultApiKeyProvider

  private object DefaultApiKeyProvider : ApiKeyProvider {
    override fun get() = ApiKey(BuildConfig.API_KEY)
  }
}
