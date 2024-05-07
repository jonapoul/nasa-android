package nasa.android.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.android.app.ApiKeyManager
import nasa.core.model.ApiKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ApiKeyModule {
  @Binds
  @Singleton
  fun apiKey(impl: ApiKeyManager): ApiKey.Provider
}
