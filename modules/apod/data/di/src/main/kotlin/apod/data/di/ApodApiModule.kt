package apod.data.di

import alakazam.android.core.IBuildConfig
import apod.core.http.buildOkHttp
import apod.core.http.buildRetrofit
import apod.core.model.NASA_API_URL
import apod.data.api.ApodApi
import apod.data.api.ApodJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
internal class ApodApiModule {
  @Provides
  fun api(buildConfig: IBuildConfig): ApodApi {
    return buildRetrofit(
      client = buildOkHttp(buildConfig.debug) { Timber.tag("APOD").v(it) },
      json = ApodJson,
      url = NASA_API_URL,
    ).create()
  }
}
