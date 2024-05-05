package apod.about.di

import alakazam.android.core.IBuildConfig
import apod.about.data.BuildConfigProvider
import apod.about.data.BuildConfigProviderImpl
import apod.about.data.GithubApi
import apod.about.data.GithubJson
import apod.core.http.buildOkHttp
import apod.core.http.buildRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
internal class AboutDataModule {
  @Provides
  fun api(buildConfig: IBuildConfig): GithubApi {
    return buildRetrofit(
      client = buildOkHttp(buildConfig.debug) { Timber.tag("GITHUB").v(it) },
      url = "https://api.github.com",
      json = GithubJson,
    ).create()
  }

  @Provides
  fun buildConfig(buildConfig: IBuildConfig): BuildConfigProvider = BuildConfigProviderImpl(
    versionName = buildConfig.versionName,
    buildTime = buildConfig.buildTime,
  )
}
