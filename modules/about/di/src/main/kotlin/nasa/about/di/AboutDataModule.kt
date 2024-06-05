package nasa.about.di

import alakazam.android.core.IBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.about.data.BuildConfigProvider
import nasa.about.data.BuildConfigProviderImpl
import nasa.about.data.GithubApi
import nasa.about.data.GithubJson
import nasa.core.http.buildOkHttp
import nasa.core.http.buildRetrofit
import retrofit2.create
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
internal class AboutDataModule {
  @Provides
  fun api(buildConfig: IBuildConfig): GithubApi = buildRetrofit(
    client = buildOkHttp(buildConfig.debug) { Timber.tag("GITHUB").v(it) },
    url = "https://api.github.com",
    json = GithubJson,
  ).create()

  @Provides
  fun buildConfig(buildConfig: IBuildConfig): BuildConfigProvider = BuildConfigProviderImpl(
    versionName = buildConfig.versionName,
    buildTime = buildConfig.buildTime,
  )
}
