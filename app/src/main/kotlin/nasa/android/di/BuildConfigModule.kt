package nasa.android.di

import alakazam.android.core.IBuildConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.android.app.NasaBuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BuildConfigModule {
  @Binds
  @Singleton
  fun buildConfig(impl: NasaBuildConfig): IBuildConfig
}
