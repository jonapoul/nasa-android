package apod.data.di

import apod.core.http.buildOkHttp
import apod.core.http.buildRetrofit
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
  fun api(): ApodApi {
    return buildRetrofit(
      client = buildOkHttp { Timber.tag("APOD").v(it) },
      json = ApodJson,
      url = "https://api.nasa.gov",
    ).create()
  }
}
