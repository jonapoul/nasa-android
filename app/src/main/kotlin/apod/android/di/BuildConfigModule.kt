package apod.android.di

import alakazam.android.core.IBuildConfig
import apod.android.app.ApodBuildConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface BuildConfigModule {
  @Binds
  @Singleton
  fun buildConfig(impl: ApodBuildConfig): IBuildConfig
}
