package nasa.android.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.android.app.PreferencesApiKeyProvider
import nasa.core.model.ApiKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {
  @Binds
  @Singleton
  fun apiKey(impl: PreferencesApiKeyProvider): ApiKey.Provider
}
