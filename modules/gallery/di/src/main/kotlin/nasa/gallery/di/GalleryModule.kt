package nasa.gallery.di

import alakazam.android.core.IBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.core.http.ApiUsageInterceptor
import nasa.core.http.buildOkHttp
import nasa.core.http.buildRetrofit
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.data.api.GalleryJson
import retrofit2.create
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class GalleryModule {
  @Provides
  fun api(
    buildConfig: IBuildConfig,
    interceptor: ApiUsageInterceptor,
  ): GalleryApi = buildRetrofit(
    client = buildOkHttp(buildConfig.debug, interceptor) { Timber.tag("GALLERY").v(it) },
    json = GalleryJson,
    url = "https://images-api.nasa.gov",
  ).create()
}
