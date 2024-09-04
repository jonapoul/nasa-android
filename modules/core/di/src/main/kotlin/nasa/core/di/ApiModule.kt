package nasa.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.core.model.ApiKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ApiModule {
  @Binds
  @Singleton
  fun apiKey(impl: PreferencesApiKeyProvider): ApiKey.Provider
}
