package nasa.apod.di

import alakazam.android.core.IBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.apod.data.api.ApodApi
import nasa.apod.data.api.ApodJson
import nasa.core.http.factories.buildOkHttp
import nasa.core.http.factories.buildRetrofit
import nasa.core.http.usage.ApiUsageInterceptor
import nasa.core.model.NASA_API_URL
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApodModule {
  @Provides
  @Singleton
  fun api(
    buildConfig: IBuildConfig,
    interceptor: ApiUsageInterceptor,
  ): ApodApi = buildRetrofit(
    client = buildOkHttp(buildConfig.debug, interceptor) { Timber.tag("APOD").v(it) },
    json = ApodJson,
    url = NASA_API_URL,
  ).create()
}
