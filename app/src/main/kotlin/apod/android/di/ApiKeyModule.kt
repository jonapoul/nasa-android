package apod.android.di

import apod.android.app.ApiKeyManager
import apod.core.model.ApiKeyProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ApiKeyModule {
  @Binds
  @Singleton
  fun apiKey(impl: ApiKeyManager): ApiKeyProvider
}
