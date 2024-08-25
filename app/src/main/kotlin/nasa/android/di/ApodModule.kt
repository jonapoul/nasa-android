package nasa.android.di

import alakazam.android.core.IBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.apod.data.api.ApodApi
import nasa.apod.data.api.ApodJson
import nasa.core.http.ApiUsageInterceptor
import nasa.core.http.buildOkHttp
import nasa.core.http.buildRetrofit
import nasa.core.model.NASA_API_URL
import retrofit2.create
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class ApodModule {
  @Provides
  fun api(
    buildConfig: IBuildConfig,
    interceptor: ApiUsageInterceptor,
  ): ApodApi = buildRetrofit(
    client = buildOkHttp(buildConfig.debug, interceptor) { Timber.tag("APOD").v(it) },
    json = ApodJson,
    url = NASA_API_URL,
  ).create()
}
